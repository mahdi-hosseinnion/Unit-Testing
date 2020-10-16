package com.example.unittesting.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.example.unittesting.models.Note
import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteViewModel
@Inject
constructor(
    val noteRepository: NoteRepository
) {
    private var _Note = MutableLiveData<Note>()

    val note: LiveData<Note>
        get() = _Note

    fun insertNote(): LiveData<Resource<Int>> {
        return note.value?.let {
            LiveDataReactiveStreams
                .fromPublisher(
                    noteRepository.insertNote(it)

                )
        } ?: object : LiveData<Resource<Int>>() {
                override fun onActive() {
                    super.onActive()
                    value = Resource.Error("Inserting note error: note is null")
                }
            }

    }


    fun setNote(note: Note) {
        _Note.value = note
    }

}