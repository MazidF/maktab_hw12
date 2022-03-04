package com.example.hw12.utils

interface MyCallback<T> {

    fun onFailure(t: Throwable)

    fun onResponse(t: T)
}