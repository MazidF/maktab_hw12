package com.example.hw12.utils

import retrofit2.Call
import retrofit2.Response

interface MyCallback<T> {

    fun onFailure(t: Throwable)

    fun onResponse(t: T?)

    fun call(call: Call<T>) {
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                onResponse(response.body())
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}