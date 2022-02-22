package com.example.hw12

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MaktabService {
    @GET("uploads/{photoName}")
    fun getImage(@Path("photoName") photoName: String): Call<ResponseBody>

    @Multipart
    @POST("users/{username}")
    fun putImage(
        @Path("username") username: String,
        @Part image: MultipartBody.Part
    ): Call<Any>

    @GET("https://www.flickr.com/services/rest")
    fun getList(
        @Query("api_key") api_key: String = "1c04e05bce6e626247758d120b372a73",
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("extras") extras: String = "url_s",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 1,
        @Query("per_page") per_page: Int = 100,
        @Query("page") page: Int = 1 ) : Call<ResponseBody>
}
