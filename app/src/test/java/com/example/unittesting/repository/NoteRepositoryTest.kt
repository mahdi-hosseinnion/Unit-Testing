package com.example.unittesting.repository

import com.example.unittesting.persistence.NoteDao
import com.example.unittesting.ui.Resource
import com.exmaple.unittesting.NoteUtil.NOTE_1
import com.exmaple.unittesting.NoteUtil.NOTE_2
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

//@RunWith(MockitoJUnitRunner::class)
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
        val returnedValue=noteRepository.updateNote(NOTE_2).blockingFirst()
        //Assert
        verify(noteDao).updateNote(NOTE_2)
        verifyNoMoreInteractions(noteDao)
        assertEquals(Resource.error<Int>("Updating note\n ERROR: Unknown error"),returnedValue)
    }

}