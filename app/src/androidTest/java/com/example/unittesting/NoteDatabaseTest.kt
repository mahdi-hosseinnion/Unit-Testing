package com.example.unittesting

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.unittesting.persistence.NoteDao
import com.example.unittesting.persistence.NoteDatabase
import org.junit.After
import org.junit.Before

public class NoteDatabaseTest {

    //system under test
    private lateinit var noteDatabase: NoteDatabase

    private fun getNoteDao(): NoteDao {
        return noteDatabase.getNoteDao()
    }

    @Before
    fun init() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).build()
    }

    @After
    fun finish() {
        noteDatabase.close()
    }


}