package com.example.unittesting.ui.noteList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.unittesting.R
import com.example.unittesting.repository.NoteRepository
import com.example.unittesting.ui.note.NoteActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class NotesListActivity : DaggerAppCompatActivity() {
    private val TAG = "NotesListActivity"

    @Inject
    lateinit var noteRepository: NoteRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)
        startActivity(Intent(this, NoteActivity::class.java))
    }
}