package com.example.unittesting.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.unittesting.models.Note
import io.reactivex.Single

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note): Single<Long>

    @Query("SELECT * FROM notes ")
    fun getAllNotes(): LiveData<List<Note>>

    @Update
    fun updateNote(note: Note): Single<Int>

    @Delete
    fun deleteNote(note: Note): Single<Int>

}