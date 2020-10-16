package com.example.unittesting.ui


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    override fun equals(other: Any?): Boolean {
        println("equlas method " + other?.javaClass?.name)
        if (other?.javaClass != javaClass ||
            (
                    other?.javaClass != Resource::class.java &&
                    other?.javaClass != Resource.Success::class.java &&
                    other?.javaClass != Resource.Error::class.java &&
                    other?.javaClass != Resource.Loading::class.java
                    )
        ) {
            println("11")
            return false
        }
        val resource = other as Resource<*>

        if (resource.data != this.data ||
            resource.message != this.message
        ) {
            println("22")
            return false
        }

        return true
    }
}
