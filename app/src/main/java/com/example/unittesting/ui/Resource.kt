package com.example.unittesting.ui


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    override fun equals(other: Any?): Boolean {

        if (other?.javaClass != javaClass ||
            other?.javaClass != Resource::class.java
        ) {
            return false
        }
        val resource = other as Resource<*>

        if (resource.data != this.data ||
            resource.message != this.message
        ) {
            return false
        }

        return true
    }
}
