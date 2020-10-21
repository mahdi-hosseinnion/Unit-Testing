package com.example.unittesting.ui.note

import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.InstantExecutorExtension
import com.example.unittesting.util.getOrAwaitValue
import com.exmaple.unittesting.NoteUtil.NOTE_1
import io.reactivex.Flowable
import io.reactivex.internal.operators.single.SingleToFlowable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeoutException

@ExtendWith(InstantExecutorExtension::class)
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
        val timedOutException = assertThrows<TimeoutException> {
            val note = noteViewModel.note.getOrAwaitValue()
        }
        //Assert
        Assertions.assertEquals("LiveData value was never set.", timedOutException.message)
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
    Insert: don't return new row without observe
     */
    @Test
    fun insertNote_dontReturnInsertRowWithoutObserve() {
        //Act
        noteViewModel.setNote(NOTE_1)
        //Assert
        verify(noteRepository, never()).insertNote(NOTE_1)
    }

    /*
    Insert a new note and observe row return
     */
    @Test
    fun insertNote_returnRow() {
        //Arrange
        val insertedRow = 1
        val returnedData: Flowable<Resource<Int>> =
            SingleToFlowable.just(Resource.success(insertedRow))
        `when`(noteRepository.insertNote(NOTE_1)).thenReturn(returnedData)
        //Act
        noteViewModel.setNote(NOTE_1)
        val returnedValue = noteViewModel.insertNote().getOrAwaitValue()
        //assert
        Assertions.assertEquals(insertedRow, returnedValue.data)

    }

    /*
        insert note didn't set any note (_Note.value) is null
        return error
     */
    @Test
    fun insertNote_NullNot_returnError() {
        //Act
        val returnedValue = noteViewModel.insertNote().getOrAwaitValue()
        //assert
        Assertions.assertEquals(
            Resource.error<Int>(msg = "Inserting note error: note is null"),
            returnedValue
        )

    }

    //just learn coverage test by rightClick on any class and Run 'Any class' with coverage

    /*
        update a note adn observe row returned
     */
    @Test
    fun updateNote_returnRow() {
        //Arrange
        val updatedRow = 1
        val returnedData: Flowable<Resource<Int>> =
            SingleToFlowable.just(Resource.success(updatedRow))
        `when`(noteRepository.updateNote(NOTE_1)).thenReturn(returnedData)
        //Act
        noteViewModel.setNote(NOTE_1)
        val returnedValue = noteViewModel.updateNote().getOrAwaitValue()
        //Assert
        Assertions.assertEquals(Resource.success(updatedRow), returnedValue)
    }

    /*
        update note didn't set any note (_Note.value) is null
        return error
     */
    @Test
    fun updateNote_NullNot_returnError() {
        //Act
        val returnedValue = noteViewModel.updateNote().getOrAwaitValue()
        //assert
        Assertions.assertEquals(
            Resource.error<Int>(msg = "Updating note error: note is null"),
            returnedValue
        )

    }

    /*
        update : don't return row without observer
     */
    @Test
    fun updateNote_dontReturnInsertRowWithoutObserve() {
        //Act
        noteViewModel.setNote(NOTE_1)
        //Assert
        verify(noteRepository, never()).updateNote(NOTE_1)
    }

}