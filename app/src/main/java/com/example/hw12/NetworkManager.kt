package com.example.hw12

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class NetworkManager {
    companion object {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        private val retrofit = Retrofit.Builder()
            .baseUrl("http://51.195.19.222")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        val service = retrofit.create(MaktabService::class.java)
    }
}
