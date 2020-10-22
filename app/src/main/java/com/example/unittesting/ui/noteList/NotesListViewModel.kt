package com.example.unittesting.ui.noteList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.unittesting.models.Note
import com.example.unittesting.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesListViewModel
@Inject
constructor(
    val repository: NoteRepository
) : ViewModel() {
    private val _Notes = MediatorLiveData<List<Note>>()

    val note: LiveData<List<Note>>
        get() = _Notes

    fun deleteNote(note: Note) = repository.deleteNote(note)

    fun getAllNotes() {
        val source = repository.getAllNotes()
        _Notes.addSource(source) { notes ->
            notes?.let {
                _Notes.value = it
            }
            _Notes.removeSource(source)
        }
    }
}