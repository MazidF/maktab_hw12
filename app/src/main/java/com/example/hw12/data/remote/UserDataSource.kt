package com.example.hw12.data.remote

import androidx.lifecycle.LiveData
import com.example.hw12.data.network.NetworkManager
import com.example.hw12.model.user.UserInfo
import com.example.hw12.model.user.UsersResponse
import com.github.leonardoxh.livedatacalladapter.Resource

class UserDataSource {
    private val service = NetworkManager.userService

    fun postUser(userInfo: UserInfo): LiveData<Resource<String>> {
        return service.createUser(userInfo)
    }

    fun getUser(id: String): LiveData<Resource<UserInfo>> {
        return service.getUser(id)
    }

    fun getUsers(): android.arch.lifecycle.LiveData<Resource<UsersResponse>> {
        return service.getUsers()
    }
}
