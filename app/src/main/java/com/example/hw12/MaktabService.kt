package com.example.hw12

import retrofit2.Call
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody

interface MaktabService {
    @GET("uploads/{photoName}")
    fun getImage(@Path("photoName") photoName: String) : Call<ResponseBody>

    @Multipart
    @POST("users/{username}")
    fun putImage(@Path("username") username: String,
                 @Part image: MultipartBody.Part) : Call<Any>
}
