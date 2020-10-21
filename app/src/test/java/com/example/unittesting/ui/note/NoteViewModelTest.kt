package com.example.unittesting.ui.note

import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.DateUtil
import com.example.unittesting.util.InstantExecutorExtension
import com.example.unittesting.util.getOrAwaitValue
import com.exmaple.unittesting.NoteUtil.NOTE_1
import com.exmaple.unittesting.NoteUtil.NOTE_2
import io.reactivex.Flowable
import io.reactivex.internal.operators.single.SingleToFlowable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
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
        assertEquals("LiveData value was never set.", timedOutException.message)
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
        assertEquals(NOTE_1, observedNote)
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
        noteViewModel.setInNewNote(true)
        noteViewModel.setNote(NOTE_1)
        val returnedValue = noteViewModel.saveNote().getOrAwaitValue()
        //assert
        assertEquals(insertedRow, returnedValue.data)

    }

    /*
        insert note didn't set any note (_Note.value) is null
        return error
     */
    @Test
    fun insertNote_NullNote_returnError() {
        //Act
        noteViewModel.setInNewNote(true)
        val returnedValue = noteViewModel.insertNote().getOrAwaitValue()
        //assert
        assertEquals(
            Resource.error<Int>(msg = "Inserting note error: note is null"),
            returnedValue
        )

    }

    /*
        update note didn't set any note (_Note.value) is null
        return error
     */
    @Test
    fun updateNote_NullNot_returnError() {
        //Act
        noteViewModel.setInNewNote(false)
        val returnedValue = noteViewModel.updateNote().getOrAwaitValue()
        //assert
        assertEquals(
            Resource.error<Int>(msg = "Updating note error: note is null"),
            returnedValue
        )

    }

    /*
        saving note didn't set any note (_Note.value) is null
        return error
     */
    @Test
    fun saveNot_NullNote_returnError() {
        //Act

        noteViewModel.setInNewNote(true)
        val returnedValue = noteViewModel.saveNote().getOrAwaitValue()
        //assert
        assertEquals(
            Resource.error<Int>(msg = "Saving note error: note is null"),
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
        noteViewModel.setInNewNote(false)
        noteViewModel.setNote(NOTE_1)
        val returnedValue = noteViewModel.saveNote().getOrAwaitValue()
        //Assert
        assertEquals(Resource.success(updatedRow), returnedValue)
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

    /*
        save note with empty content
        should return error
     */
    @Test
    fun saveNote_emptyContent_returnError() {
        //Act
        noteViewModel.setNote(NOTE_1.copy(content = "   "))
        val returnedValue = noteViewModel.saveNote().getOrAwaitValue()
        //Assert
        assertEquals(
            Resource.error<Int>("The content of note should not be null"),
            returnedValue
        )
    }

    /*
        updateNote: update content and title of note
     */
    @Test
    fun updateNote_updateContentAndTitle() {
        //Arrange
        noteViewModel.setNote(NOTE_1)
        //Act
        noteViewModel.updateNote(NOTE_2.title, NOTE_2.content)
        val timeStamp = DateUtil.getCurrentTimeStamp()
        val updatedNote = noteViewModel.note.value
        //Assert
        assertNotNull(updatedNote)
        assertEquals(NOTE_2.title, updatedNote?.title)
        assertEquals(NOTE_2.content, updatedNote?.content)
        assertEquals(timeStamp, updatedNote?.timeStamp)
    }

    /*
        updateNote: update with empty and null content should not update
     */
    @Test
    fun updateNote_NullContent_shouldNotUpdate() {
        //Arrange
        noteViewModel.setNote(NOTE_1)
        //Act
        val mainNote = noteViewModel.note.value
        noteViewModel.updateNote(NOTE_2.title, "      ")
        val emptyNote = noteViewModel.note.value
        noteViewModel.updateNote(NOTE_2.title, null)
        val nullNote = noteViewModel.note.value

        //Assert
        assertNotNull(mainNote)
        assertNotNull(emptyNote)
        assertNotNull(nullNote)
        assertEquals(mainNote, emptyNote)
        assertEquals(mainNote, nullNote)
        assertEquals(nullNote, emptyNote)
    }


}