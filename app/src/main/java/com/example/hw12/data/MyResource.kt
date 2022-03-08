package com.example.hw12.data

class MyResource <T> private constructor() {
    var resource: T? = null
        private set
    var error: Throwable? = null
        private set
    val isSuccess: Boolean
        get() = resource != null && error == null

    companion object {
        fun <T> success(body: T): MyResource<T> {
            val resource: MyResource<T> = MyResource()
            resource.resource = body
            return resource
        }

        fun <T> error(error: Throwable?): MyResource<T> {
            val resource: MyResource<T> = MyResource()
            resource.error = error
            return resource
        }
    }
}