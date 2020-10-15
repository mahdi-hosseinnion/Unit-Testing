package com.example.unittesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.util.getOrAwaitValue
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class NotesListActivity : DaggerAppCompatActivity() {
    private val TAG = "NotesListActivity"

//    @set:Inject
//    var noteRepository: NoteRepository? = null
    @Inject
    lateinit var noteRepository:NoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)
        Log.d(TAG, "onCreate: noteRepository: ${noteRepository}")
    }
}