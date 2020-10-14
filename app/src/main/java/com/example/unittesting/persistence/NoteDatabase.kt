package com.example.unittesting.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unittesting.models.Note


@Database(entities = [Note::class], version = 2)
public abstract class NoteDatabase : RoomDatabase() {

    public abstract fun getNoteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }
}