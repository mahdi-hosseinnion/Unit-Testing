package com.example.unittesting

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.unittesting.persistence.NoteDao
import com.example.unittesting.persistence.NoteDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import androidx.arch.core.executor.testing.CountingTaskExecutorRule

public abstract class NoteDatabaseTest {

    //system under test
    private lateinit var noteDatabase: NoteDatabase
//    @Rule
//    @JvmField
//    val countingTaskExecutorRule = CountingTaskExecutorRule()
    fun getNoteDao(): NoteDao {
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