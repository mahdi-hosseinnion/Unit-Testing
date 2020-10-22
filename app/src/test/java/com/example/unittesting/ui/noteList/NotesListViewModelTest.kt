package com.example.unittesting.ui.noteList

import androidx.lifecycle.LiveData
import com.example.unittesting.models.Note
import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.InstantExecutorExtension
import com.example.unittesting.util.StaticMethods.createLiveDataWithInitialData
import com.example.unittesting.util.getOrAwaitValue
import com.exmaple.unittesting.NoteUtil
import com.exmaple.unittesting.NoteUtil.NOTE_2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@ExtendWith(InstantExecutorExtension::class)
class NotesListViewModelTest {
    //system under test
    private lateinit var notesListViewModel: NotesListViewModel

    @Mock
    lateinit var noteRepository: NoteRepository

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
        notesListViewModel = NotesListViewModel(noteRepository)
    }

    /*
        Retrieve list of notes
        observe list
        return list
     */
    @Test
    fun retrieveList_returnList() {
        //Arrange
        val listOfNotes = NoteUtil.getListOfNote()
        val returnedData: LiveData<List<Note>> = createLiveDataWithInitialData(
            aValue = {
                listOfNotes
            }
        )
        Mockito.`when`(noteRepository.getAllNotes()).thenReturn(returnedData)
        //Act
        notesListViewModel.getAllNotes()
        val returnedValue = notesListViewModel.note.getOrAwaitValue()
        //Assert
        assertEquals(listOfNotes, returnedValue)
    }

    /*
     Retrieve list of notes
     observe list
     return empty list
  */
    @Test
    fun retrieveList_emptyList_returnList() {
        //Arrange
        val listOfNotes = ArrayList<Note>()
        val returnedData: LiveData<List<Note>> = createLiveDataWithInitialData(
            aValue = {
                listOfNotes
            }
        )
        Mockito.`when`(noteRepository.getAllNotes()).thenReturn(returnedData)
        //Act
        notesListViewModel.getAllNotes()
        val returnedValue = notesListViewModel.note.getOrAwaitValue()
        //Assert
        assertEquals(listOfNotes, returnedValue)
    }

    /*
        delete note
        observe Resource.Success
        return Resource.Success
     */
    @Test
    fun deleteNote_returnSuccess() {
        //Arrange
        val returnedRow = 1
        val returnedData= createLiveDataWithInitialData {
            Resource.success(returnedRow)
        }
        Mockito.`when`(noteRepository.deleteNote(NOTE_2)).thenReturn(returnedData)
        //Act
        val returnedValue = notesListViewModel.deleteNote(NOTE_2).getOrAwaitValue()
        //Assert
        assertEquals(Resource.success(returnedRow),returnedValue)

    }
    /*
        delete note
        observe Resource.Success
        return Resource.Success
     */
    @Test
    fun deleteNote_returnError() {
        //Arrange
        val returnedRow = -1
        val returnedData= createLiveDataWithInitialData {
            Resource.error<Int>("Deleting note\n ERROR: Unknown error")
        }
        Mockito.`when`(noteRepository.deleteNote(NOTE_2)).thenReturn(returnedData)
        //Act
        val returnedValue = notesListViewModel.deleteNote(NOTE_2).getOrAwaitValue()
        //Assert
        assertEquals(Resource.error<Int>("Deleting note\n ERROR: Unknown error"),returnedValue)

    }


}