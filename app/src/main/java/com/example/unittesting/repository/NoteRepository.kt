package com.example.unittesting.repository

import com.example.unittesting.models.Note
import com.example.unittesting.persistence.NoteDao
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.Constants.NETWORK_DELAY
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.annotations.SchedulerSupport.IO
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository
@Inject
constructor(
    val noteDao: NoteDao
) {


    fun insertNote(note: Note): Flowable<Resource<Int>> = noteDao.insertNote(note)
        .delaySubscription(NETWORK_DELAY, TimeUnit.MILLISECONDS)//for testing
        //convert long to int b/c all other action return integer
        .map { long ->
            long.toInt()
        }
        .onErrorReturn { throwable ->
            throwable.printStackTrace()
            -1
        }
        .map { int ->
            if (int > 0) {
                //success
                Resource.Success(int)
            } else {
                //error
                Resource.Error("Inserting new note\n ERROR: Unknown error")
            }
        }
        .subscribeOn(Schedulers.io())
        .toFlowable()

}