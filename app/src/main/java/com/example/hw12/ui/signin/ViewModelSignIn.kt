package com.example.hw12.ui.signin

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw12.data.MyRepository
import com.example.hw12.model.user.User
import com.example.hw12.model.user.UsersResponse
import com.github.leonardoxh.livedatacalladapter.Resource
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream

class ViewModelSignIn(
    private val repository: MyRepository = MyRepository
) : ViewModel() {
    val bitmap by lazy {
        MutableLiveData<Bitmap>()
    }
    val userInfo = Array(6) { "" }

    fun post(bitmap: Bitmap?): Pair<MutableLiveData<Boolean>, LiveData<Resource<String>>> {
        val user: User = createUser()
        val bytes = getByteArray(bitmap) ?: byteArrayOf()
        val body = MultipartBody.create(MediaType.parse("image/*"), bytes)
        val image = MultipartBody.Part.createFormData("image", user.email + ".png", body)
        return repository.putUser(user.toUserInfo(), image)
    }

    fun get(id: String, email: String): Pair<MutableLiveData<Boolean>, MutableLiveData<User>> {
        return repository.getUser(id, email)
    }

    private fun getByteArray(bitmap: Bitmap?): ByteArray? {
        return if(bitmap != null) {
            val output = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.toByteArray()
        } else {
            null
        }
    }

    private fun createUser(): User {
        var i = 0
        return User(userInfo[i++], userInfo[i++], userInfo[i++]).apply {
            userName = userInfo[i++]
            phone = userInfo[i++]
            birthday = userInfo[i]
        }
    }

    fun getUsers(): android.arch.lifecycle.LiveData<Resource<UsersResponse>> {
        return repository.getUsers()
    }
}