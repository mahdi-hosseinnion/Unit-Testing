package com.example.unittesting.repository

import com.example.unittesting.persistence.NoteDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository
@Inject
constructor(
    noteDao: NoteDao
) {

}