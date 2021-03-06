package com.example.unittesting.ui.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unittesting.models.Note
import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.Constants.ACTION_INSERT
import com.example.unittesting.util.Constants.ACTION_UPDATE
import com.example.unittesting.util.Constants.NOTE_TITLE_NULL
import com.example.unittesting.util.DateUtil
import com.example.unittesting.util.StaticMethods.createResourceErrorLiveData
import org.reactivestreams.Subscription
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteViewModel
@Inject
constructor(
    val noteRepository: NoteRepository
) : ViewModel() {
    private val TAG = "NoteViewModel"

    private var _Note = MutableLiveData<Note>()
    private var _ViewState = MutableLiveData<NoteViewState>()

    private var isNewNote: Boolean = true

    private var updateSubscription: Subscription? = null
    private var insertSubscription: Subscription? = null
    val note: LiveData<Note>
        get() = _Note
    val viewState: LiveData<NoteViewState>
        get() = _ViewState

    fun insertNote(): LiveData<Resource<Int>> {
        return note.value?.let { note ->
            LiveDataReactiveStreams
                .fromPublisher(
                    noteRepository.insertNote(note)
                        .doOnSubscribe {
                            insertSubscription = it
                        }

                )
        } ?: createResourceErrorLiveData("Inserting note error: note is null")
    }

    fun updateNote(): LiveData<Resource<Int>> {
        return note.value?.let { note ->
            LiveDataReactiveStreams.fromPublisher(
                noteRepository.updateNote(note)
                    .doOnSubscribe {
                        updateSubscription = it
                    }
            )
        } ?:createResourceErrorLiveData("Updating note error: note is null")
    }

    fun updateNote(title: String, content: String?) {
        if (note.value == null) {
            return
        }
        if (!content.isNullOrBlank()) {
            val updatedNote: Note = Note(note.value!!)
                .copy(
                    title = title,
                    content = content,
                    timeStamp = DateUtil.getCurrentTimeStamp()
                )
            _Note.value = updatedNote
        }
    }

    fun saveNote(): LiveData<Resource<Int>> {
        if(note.value==null){
            return createResourceErrorLiveData("Saving note error: note is null")
        }
        if (!shouldAllowSave()) {
            return createResourceErrorLiveData("The content of note should not be null")
        }
        cancelPendingTransaction()
        return object : NoteInsertUpdateHelper<Int>() {
            override fun setNoteId(i: Int) {
                isNewNote = false
                val note = note.value?.copy(id = i)
                _Note.value = note
            }

            override fun defineAction(): String {
                return if (isNewNote) ACTION_INSERT else ACTION_UPDATE

            }

            override fun onTransactionComplete() {
                updateSubscription = null
                insertSubscription = null
            }

            override fun getAction(): LiveData<Resource<Int>> {
                return if (isNewNote) insertNote() else updateNote()
            }
        }.getAsLiveData()
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

    fun cancelPendingTransaction() {
        cancelInsertTransaction()
        cancelUpdateTransaction()
    }

    fun cancelInsertTransaction() {
        insertSubscription?.cancel()
        insertSubscription = null
    }

    fun cancelUpdateTransaction() {
        updateSubscription?.cancel()
        updateSubscription = null
    }

    private fun shouldAllowSave(): Boolean {
        note.value?.content?.let {
            return it.isNotBlank()
        }
        return false
    }

    fun shouldNavigateBack(): Boolean =
        viewState.value == NoteViewState.VIEW


}