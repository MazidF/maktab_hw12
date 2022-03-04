package com.example.hw12.data.network

import androidx.lifecycle.LiveData
import com.github.leonardoxh.livedatacalladapter.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ImageService {
    @GET("uploads/{photoName}")
    fun getImage(@Path("photoName") photoName: String): LiveData<Resource<ResponseBody>>

    @Multipart
    @POST("users/{username}")
    fun putImage(
        @Path("username") username: String,
        @Part image: MultipartBody.Part
    ): LiveData<Resource<ResponseBody>>
}
