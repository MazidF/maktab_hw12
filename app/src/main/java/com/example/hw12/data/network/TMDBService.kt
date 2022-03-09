package com.example.hw12.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {
    companion object {
        const val TOKEN = "f5a5d7af59cba941f2d09648869ea4e3"
    }

    @GET("")
    fun search(
        @Query("language") language: String = "en",
    )
}