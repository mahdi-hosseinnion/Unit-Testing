package com.example.unittesting.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.example.unittesting.models.Note
import com.example.unittesting.persistence.NoteDao
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.Constants.NETWORK_DELAY
import com.example.unittesting.util.StaticMethods.createResourceErrorLiveData
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.annotations.SchedulerSupport.IO
import io.reactivex.internal.operators.single.SingleToFlowable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
//open class NoteRepository
class NoteRepository
@Inject
constructor(
    val noteDao: NoteDao
) {


    fun insertNote(note: Note): Flowable<Resource<Int>> {
        return noteDao.insertNote(note)
            .delaySubscription(NETWORK_DELAY, TimeUnit.MILLISECONDS)//for testing
            //convert long to int b/c all other action return integer
            .map { long ->
                return@map long.toInt()
            }
            .onErrorReturn { throwable ->
                throwable.printStackTrace()
                return@onErrorReturn -1
            }
            .map { int ->
                if (int > 0) {
                    //success
                    return@map Resource.success(int)
                } else {
                    //error
                    return@map Resource.error("Inserting new note\n ERROR: Unknown error", null)
                }
            }
            .subscribeOn(Schedulers.io())
            .toFlowable()
    }

    fun updateNote(note: Note): Flowable<Resource<Int>> = noteDao.updateNote(note)
        .delaySubscription(NETWORK_DELAY, TimeUnit.MILLISECONDS)
        .onErrorReturn { throwable ->
            throwable.printStackTrace()
            -1
        }
        .map { aInt ->
            if (aInt > 0) {
                Resource.success(aInt)
            } else {
                Resource.error("Updating note\n ERROR: Unknown error")
            }
        }
        .subscribeOn(Schedulers.io())
        .toFlowable()

    fun deleteNote(note: Note): LiveData<Resource<Int>> {
        if (note.id < 1) {
            return createResourceErrorLiveData("delete note \n Error: incorrect id for note \n Note id = ${note.id}")
        }
        return LiveDataReactiveStreams.fromPublisher(
            noteDao.deleteNote(note)
                .delaySubscription(NETWORK_DELAY, TimeUnit.MILLISECONDS)
                .onErrorReturn {
                    it.printStackTrace()
                    -1
                }
                .map { aInt ->
                    if (aInt > 0) {
                        Resource.success(aInt)
                    } else {
                        Resource.error("Deleting note\n ERROR: Unknown error")
                    }
                }
                .subscribeOn(Schedulers.io())
                .toFlowable()

        )
    }

    fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()


}