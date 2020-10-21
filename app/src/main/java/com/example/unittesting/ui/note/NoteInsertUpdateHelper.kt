package com.example.unittesting.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.unittesting.models.Note
import com.example.unittesting.ui.Resource
import com.example.unittesting.util.Constants.ACTION_INSERT
import com.example.unittesting.util.Constants.GENERIC_ERROR
import java.lang.Exception

abstract class NoteInsertUpdateHelper<T> {
    private var result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)
        try {
            val source = getAction()
            result.addSource(source) { resource ->
                result.removeSource(source)
                result.value = resource
                setNewNoteIdIfIsNewNote(resource)
                onTransactionComplete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            result.value = Resource.error(GENERIC_ERROR)
        }
    }

    private fun setNewNoteIdIfIsNewNote(resource: Resource<T>?) {
        resource?.let { resource ->
            if (resource.data is Int) {
                val i = resource.data as Int
                if (defineAction() == ACTION_INSERT) {
                    if (i >= 1) {
                        setNoteId(i)
                    }
                }
            }
        }
    }

    abstract fun setNoteId(i: Int)

    abstract fun defineAction(): String

    abstract fun onTransactionComplete()

    abstract fun getAction(): LiveData<Resource<T>>

    fun getAsLiveData(): LiveData<Resource<T>> = result
}