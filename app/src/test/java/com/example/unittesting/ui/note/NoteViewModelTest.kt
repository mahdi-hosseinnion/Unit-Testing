package com.example.unittesting.ui.note

import com.example.unittesting.models.Note
import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.getOrAwaitValue
import com.exmaple.unittesting.NoteUtil.NOTE_1
import io.reactivex.Flowable
import io.reactivex.internal.operators.single.SingleToFlowable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NoteViewModelTest {
    //system under test
    lateinit var noteViewModel: NoteViewModel

    @Mock
    lateinit var noteRepository: NoteRepository

    @BeforeEach
    fun initMock() {
        MockitoAnnotations.initMocks(this)
        noteViewModel = NoteViewModel(noteRepository)
    }

    /*
    can't observe note that hasn't been set
     */
    @Test
    fun observeEmptyNoteWhenNoteSet() {
        //Act
        val note = noteViewModel.note.getOrAwaitValue()
        //Assert
        Assertions.assertNull(note)
    }

    /*
     observe a note has been set and onChange will trigger an activity
     */
    @Test
    fun observerNote_whenSet() {
        //Act
        noteViewModel.setNote(NOTE_1)
        val observedNote = noteViewModel.note.getOrAwaitValue()
        //Assert
        Assertions.assertEquals(NOTE_1, observedNote)
    }

    /*
    Insert a new note and observe row return
     */
    @Test
    fun insertNote_returnRow() {
        //Arrange
        val insertedRow = 1
        val returnedData:Flowable<Resource<Int>> = SingleToFlowable.just(Resource.success(insertedRow))
        `when`(noteRepository.insertNote(NOTE_1)).thenReturn(returnedData)
        //Act
        noteViewModel.setNote(NOTE_1)
        val returnedValue=noteViewModel.insertNote().getOrAwaitValue()
        //assert
        Assertions.assertEquals(insertedRow,returnedValue)

    }
    /*
    Insert: don't return new row without observe
     */
    @Test
    fun insertNote_dontReturnInsertRowWithoutObserve(){
        //Act
        noteViewModel.setNote(NOTE_1)
        //Assert
        verify(noteRepository, never()).insertNote(NOTE_1)
    }

}