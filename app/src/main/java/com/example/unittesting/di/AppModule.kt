package com.example.unittesting.di

import android.app.Application
import androidx.room.Room
import com.example.unittesting.persistence.NoteDao
import com.example.unittesting.persistence.NoteDatabase
import com.example.unittesting.persistence.NoteDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideNoteDataBase(application: Application): NoteDatabase {
        return Room.databaseBuilder(
            application,
            NoteDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(appDb: NoteDatabase): NoteDao {
        return appDb.getNoteDao()
    }
}
