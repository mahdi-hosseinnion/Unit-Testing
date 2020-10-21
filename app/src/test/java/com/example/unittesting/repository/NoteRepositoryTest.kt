package com.example.unittesting.repository

import androidx.lifecycle.LiveData
import com.example.unittesting.models.Note
import com.example.unittesting.persistence.NoteDao
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.InstantExecutorExtension
import com.example.unittesting.util.LiveDataTestUtil
import com.example.unittesting.util.StaticMethods.createLiveDataWithInitialData
import com.example.unittesting.util.getOrAwaitValue
import com.exmaple.unittesting.NoteUtil.NOTE_1
import com.exmaple.unittesting.NoteUtil.NOTE_2
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.Mockito.*

//@RunWith(MockitoJUnitRunner::class)
@ExtendWith(InstantExecutorExtension::class)
class NoteRepositoryTest {
    //system under test
    lateinit var noteRepository: NoteRepository

    //    @Mock
    lateinit var noteDao: NoteDao

    @BeforeEach
    public fun init() {
        println("init called")
//        MockitoAnnotations.initMocks(this)
        noteDao = Mockito.mock(NoteDao::class.java)
        noteRepository = NoteRepository(noteDao)
    }

    /*
        insert note
        verify the correct method is called
        confirm observer is trigger
        confirm new row inserted
     */
    @Test
    fun insertNote_returnRow() {
        //Arrange
        val insertedRow: Long = 1L
        val returnedData: Single<Long> = Single.just(insertedRow)
//        when(noteDao.insertNote(any(Note::class))).then
        `when`(noteDao.insertNote(NOTE_1)).thenReturn(returnedData)
        //Act
        val returnedValue: Resource<Int> =
            noteRepository.insertNote(NOTE_1).blockingSingle()
        //Assert
        verify(noteDao).insertNote(NOTE_1)
        verifyNoMoreInteractions(noteDao)
        println("insertNote: $returnedValue.")
        assertEquals(Resource.success(1), returnedValue)
        //or test using RxJava
//        noteRepository.insertNote(NoteUtil.NOTE_1)
//            .test()
//            .await()
//            .assertValues(Resource.Success(1))

    }

    @Test
    fun insertNote_returnFailure() {
        //Arrange
        val failureNumber: Long = -1L
        val returnedData: Single<Long> = Single.just(failureNumber)
        `when`(noteDao.insertNote(NOTE_1)).thenReturn(returnedData)
        //Act
        val returnValue: Resource<Int> = noteRepository.insertNote(NOTE_1).blockingSingle()
        //Assert
        verify(noteDao).insertNote(NOTE_1)
        verifyNoMoreInteractions(noteDao)
        assertEquals(
            Resource.error<Int>("Inserting new note\n ERROR: Unknown error", null),
            returnValue
        )

    }

    /*
        Update a note,
        verify correct method is called
        confirm observer trigger
        confirm number of rows updated
     */
    @Test
    fun updateNote_returnNumberOfUpdate() {
        //Arrange
        val updatedRow = 1
        val returnedData = Single.just(updatedRow)
        `when`(noteDao.updateNote(NOTE_2)).thenReturn(returnedData)
        //Act
        val returnedValue = noteRepository.updateNote(NOTE_2).blockingFirst()
        //Assert
        verify(noteDao).updateNote(NOTE_2)
        verifyNoMoreInteractions(noteDao)

        Assertions.assertEquals(Resource.success(updatedRow), returnedValue)
    }

    /*
        update note
        failure (-1)
     */
    @Test
    fun updateNote_returnFailure() {
        //Arrange
        val failureNumber = -1
        val returnedData = Single.just(failureNumber)
        `when`(noteDao.updateNote(NOTE_2)).thenReturn(returnedData)
        //Act
        val returnedValue = noteRepository.updateNote(NOTE_2).blockingFirst()
        //Assert
        verify(noteDao).updateNote(NOTE_2)
        verifyNoMoreInteractions(noteDao)
        assertEquals(Resource.error<Int>("Updating note\n ERROR: Unknown error"), returnedValue)
    }

    /*
        delete note
        wrong id (-1)
        return error
     */
    @ParameterizedTest
    @ValueSource(ints = [0, -1])
    fun deleteNote_incorrectId_returnError(wrongId: Int) {
        //Act
        val returnedValue = noteRepository.deleteNote(NOTE_1.copy(id = wrongId)).getOrAwaitValue()
        //Assert
        assertEquals(
            Resource.error<Int>("delete note \n Error: incorrect id for note \n Note id = ${wrongId}"),
            returnedValue
        )

    }

    /*
        delete note
        delete success
        return success with deleted row
     */
    @Test
    fun deleteNote_returnSuccess() {
        //Arrange
        val row = 1
        val returnedData = Single.just(row)
        `when`(noteDao.deleteNote(NOTE_2)).thenReturn(returnedData)
        //Act
        val returnedValue = noteRepository.deleteNote(NOTE_2).getOrAwaitValue()
        //Assert
        assertEquals(Resource.success(row), returnedValue)
    }

    /*
        delete note
        delete failure
        return Resource.Error
     */
    @Test
    fun deleteNote_returnFailure() {
        //Arrange
        val row = -1
        val returnedData = Single.just(row)
        `when`(noteDao.deleteNote(NOTE_2)).thenReturn(returnedData)
        //Act
        val returnedValue = noteRepository.deleteNote(NOTE_2).getOrAwaitValue()
        //Assert
        assertEquals(
            Resource.error<Int>("Deleting note\n ERROR: Unknown error"),
            returnedValue
        )
    }

    /*
        retrieve notes
        return list of notes
     */
    @Test
    fun retrieveNote_returnListOfNote() {
        //Arrange
        val returnedData: LiveData<List<Note>> = createLiveDataWithInitialData(
            aValue = {
                val listOfNote = ArrayList<Note>()
                for (i in 0..10) {
                    listOfNote.add(NOTE_1.copy(id = (i * 11)))
                }
                listOfNote
            }
        )
        printAllMembersOfIt(returnedData)
        `when`(noteDao.getAllNotes()).thenReturn(returnedData)
        //Act
        val returnedValue = noteRepository.getAllNotes().getOrAwaitValue()
        //Assert
        val mainList = returnedData.getOrAwaitValue()
        assertEquals(mainList, returnedValue)
    }

    /*
        retrieve notes
        return empty list
     */
    @Test
    fun retrieveNote_emptyList_returnEmptyList() {
        //Arrange
        val returnedData: LiveData<List<Note>> = createLiveDataWithInitialData(
            aValue = {
                val listOfNote = ArrayList<Note>()
                listOfNote
            }
        )
        printAllMembersOfIt(returnedData)
        `when`(noteDao.getAllNotes()).thenReturn(returnedData)
        //Act
        val returnedValue = noteRepository.getAllNotes().getOrAwaitValue()
        //Assert
        val mainList = returnedData.getOrAwaitValue()
        assertEquals(mainList, returnedValue)
    }
    //for testing the test
    private fun printAllMembersOfIt(source: LiveData<List<Note>>) {
        val items: List<Note> = source.getOrAwaitValue()
        if (items.isEmpty()) {
            println("item is empty")
            return
        }
        for (item in items) {
            println("note id: ${item.id}  and note title: ${item.title}")
        }
    }

}