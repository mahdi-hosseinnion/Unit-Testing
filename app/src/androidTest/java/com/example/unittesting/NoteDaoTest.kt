package com.example.unittesting

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.unittesting.models.Note
import com.example.unittesting.util.getOrAwaitValue
import com.exmaple.unittesting.NoteUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


private const val TAG = "NoteDaoTest"

@RunWith(AndroidJUnit4::class)
class NoteDaoTest : NoteDatabaseTest() {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    /*
        Insert, Read, Delete
     */
    @Test
    fun insertReadDelete() {
        val note = Note(NoteUtil.NOTE_1)
        //insert
        getNoteDao().insertNote(note).blockingGet()
        //read
        var insertedNote = getNoteDao().getAllNotes().getOrAwaitValue()
        assertNotNull(insertedNote)
        print("note id = ${insertedNote[0].id}")
        Log.e(TAG, "insertReadDelete: note id = ${insertedNote[0].id}")
        note.id = insertedNote[0].id
        assertEquals(note, insertedNote[0])
        //delete
        getNoteDao().deleteNote(note).blockingGet()
        //confirm the database is empty
        insertedNote = getNoteDao().getAllNotes().getOrAwaitValue()
        assertEquals(0, insertedNote.size)
    }

    /*
        Insert, Read, Update, Read, Delete
     */

    @Test
    fun insertReadUpdateReadDelete() {
        val note = Note(NoteUtil.NOTE_1)
        //insert
        getNoteDao().insertNote(note).blockingGet()
        //read
        var insertedNote = getNoteDao().getAllNotes().getOrAwaitValue()
        assertNotNull(insertedNote)
        print("note id = ${insertedNote[0].id}")
        Log.e(TAG, "insertReadDelete: note id = ${insertedNote[0].id}")
        note.id = insertedNote[0].id
        assertEquals(note, insertedNote[0])
        //update
        note.title = NoteUtil.TITLE_2
        getNoteDao().updateNote(note).blockingGet()
        //read
        val updatedNote = getNoteDao().getAllNotes().getOrAwaitValue()
        assertNotNull(updatedNote)
        assertEquals(note, updatedNote[0])
        //delete
        getNoteDao().deleteNote(note).blockingGet()
        //confirm the database is empty
        insertedNote = getNoteDao().getAllNotes().getOrAwaitValue()
        assertEquals(0, insertedNote.size)
    }
    /*
        Insert note with null title, should throw exception
     */

//case completed cannot cast title to null
    //    @Test(expected = SQLiteConstraintException::class)
//    @Throws(Exception::class)
    @Test
    fun insert_nullTitle_throwSQLiteConstraintException() {
        val note = Note(NoteUtil.NOTE_1)
        //case completed cannot cast title to null
//        note.title = null
        // insert
        getNoteDao().insertNote(note).blockingGet()
    }

    /*
        Update note with null title, should throw exception
     */

    //case completed cannot cast title to null
//    @Test(expected = SQLiteConstraintException::class)
//    @Throws(Exception::class)
    @Test
    fun updateNote_nullTitle_throwSQLiteConstraintException() {
        var note = Note(NoteUtil.NOTE_1)
        // insert
        getNoteDao().insertNote(note).blockingGet()
        // read

        val insertedNotes = getNoteDao().getAllNotes().getOrAwaitValue()
        assertNotNull(insertedNotes)

        // update

        // update
        note = Note(insertedNotes[0])
        //case completed cannot cast title to null
//        note.title = null
        getNoteDao().updateNote(note).blockingGet()
    }

}