package com.example.hw12.data

import androidx.lifecycle.LiveData
import com.example.hw12.data.network.NetworkManager
import com.example.hw12.model.UserInfo
import com.example.hw12.utils.MyCallback
import com.github.leonardoxh.livedatacalladapter.Resource

class UserDataSource {
    private val service = NetworkManager.userService

    fun postUser(userInfo: UserInfo): LiveData<Resource<String>> {
        return service.createUser(userInfo)
    }

    fun getUser(id: String): LiveData<Resource<UserInfo>> {
        return service.getUser(id)
    }
}
