package com.example.unittesting.util

import androidx.lifecycle.LiveData
import com.example.unittesting.ui.Resource

object StaticMethods {
    public fun <T> createResourceErrorLiveData(message: String = "UNKNOWN ERROR"): LiveData<Resource<T>> {
        return object : LiveData<Resource<T>>() {
            override fun onActive() {
                super.onActive()
                value = Resource.error(message)
            }
        }
    }
}