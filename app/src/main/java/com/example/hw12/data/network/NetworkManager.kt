package com.example.hw12.data.network

import com.example.hw12.model.User
import com.example.hw12.model.UserInfo
import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory
import com.google.gson.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object NetworkManager {
    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(crateInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private fun crateInterceptor(): Interceptor {
        return Interceptor { chain ->
            val oldRequest = chain.request()
            val newRequest = oldRequest.newBuilder()
                .addHeader("Authorization", "Bearer Mazid_App_Token!!!")
                .build()
            chain.proceed(newRequest)
        }
    }

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(User::class.java, userAdapter())
        .setLenient()
        .create()

    private fun userAdapter() = JsonDeserializer { json, _, _ ->
        val jsonObject = json.asJsonObject
        User.fromJsonObject(jsonObject)
    }

    private val retrofitUser = Retrofit.Builder()
        .baseUrl("http://papp.ir/api/v1/")
        .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
        .addConverterFactory(LiveDataResponseBodyConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    private val retrofitImage: Retrofit = retrofitUser.newBuilder()
        .baseUrl("http://51.195.19.222/")
        .build()

    val userService = retrofitUser.create(UserService::class.java)
    val imageService = retrofitImage.create(ImageService::class.java)
}

