package com.example.hw12.data

import androidx.lifecycle.LiveData
import com.example.hw12.data.network.NetworkManager
import com.github.leonardoxh.livedatacalladapter.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call

class ImageDataSource {
    private val service = NetworkManager.imageService

    fun postImage(email: String, image: MultipartBody.Part): LiveData<Resource<ResponseBody>> {
        return service.putImage(email, image)
    }

    fun getImage(email: String): LiveData<Resource<ResponseBody>> {
        return service.getImage(email)
    }
}