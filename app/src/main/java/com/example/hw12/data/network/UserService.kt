package com.example.hw12.data.network

import androidx.lifecycle.LiveData
import com.example.hw12.model.UserInfo
import com.github.leonardoxh.livedatacalladapter.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @POST("users")
    fun createUser(
        @Body userInfo: UserInfo
    ): LiveData<Resource<String>>

    @GET("users/{id}")
    fun getUser(
        @Path("id") id: String
    ): LiveData<Resource<UserInfo>>

}
