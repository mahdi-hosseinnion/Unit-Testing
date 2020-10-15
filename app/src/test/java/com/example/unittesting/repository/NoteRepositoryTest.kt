package com.example.unittesting.repository

import com.example.unittesting.persistence.NoteDao
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

//@RunWith(MockitoJUnitRunner::class)
class NoteRepositoryTest {
    //system under test
    lateinit var noteRepository:NoteRepository

//    @Mock
    lateinit var noteDao: NoteDao

    @BeforeEach
    public fun init(){
        print("init called")
//        MockitoAnnotations.initMocks(this)
        noteDao= Mockito.mock(NoteDao::class.java)
        noteRepository= NoteRepository(noteDao)
    }

    @Test
    fun dummyTest(){
        Assert.assertNotNull(noteRepository)
        Assert.assertNotNull(noteDao)
    }


}