package com.example.unittesting.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unittesting.models.Note
import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.Constants.NOTE_TITLE_NULL
import com.example.unittesting.util.DateUtil
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteViewModel
@Inject
constructor(
    val noteRepository: NoteRepository
) : ViewModel() {
    private var _Note = MutableLiveData<Note>()
    private var _ViewState = MutableLiveData<NoteViewState>()

    private var isNewNote: Boolean = true

    val note: LiveData<Note>
        get() = _Note
    val viewState: LiveData<NoteViewState>
        get() = _ViewState

    fun insertNote(): LiveData<Resource<Int>> {
        return note.value?.let {
            LiveDataReactiveStreams
                .fromPublisher(
                    noteRepository.insertNote(it)

                )
        } ?: object : LiveData<Resource<Int>>() {
            override fun onActive() {
                super.onActive()
                value = Resource.error("Inserting note error: note is null", null)
            }
        }

    }

    fun updateNote(title: String, content: String) {
        if (note.value == null) {
            return
        }
        val temp = removeWhiteSpace(content)
        if (temp.isNotEmpty()) {
            val updatedNote: Note = Note(note.value!!)
            updatedNote.title = title
            updatedNote.content = content
            updatedNote.timeStamp = DateUtil.getCurrentTimeStamp()

            _Note.value = updatedNote
        }
    }

    fun saveNote(): LiveData<Resource<Int>>? {
        return null
    }

    fun setViewState(viewState: NoteViewState) {
        _ViewState.value = viewState
    }

    fun setInNewNote(isNewNote: Boolean) {
        this.isNewNote = isNewNote
    }

    fun setNote(note: Note) {
        if (note.title.isBlank()) {
            throw Exception(NOTE_TITLE_NULL)
        }
        _Note.value = note
    }

    private fun removeWhiteSpace(text: String): String {
        var string = text
        string = string.replace("\n", "")
        string = string.replace(" ", "")
        return string
    }

    fun shouldNavigateBack(): Boolean =
        viewState.value == NoteViewState.VIEW

}