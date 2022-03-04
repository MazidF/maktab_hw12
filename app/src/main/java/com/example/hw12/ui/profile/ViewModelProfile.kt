package com.example.hw12.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw12.data.MyRepository
import com.example.hw12.model.User
import com.example.hw12.model.UserInfo
import com.github.leonardoxh.livedatacalladapter.Resource
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream

class ViewModelProfile(
    private val repository: MyRepository = MyRepository()
) : ViewModel() {

    fun post(user: User, imageName: String, bitmap: Bitmap?): Pair<MutableLiveData<Boolean>, LiveData<Resource<String>>> {
        val bytes = getByteArray(bitmap) ?: byteArrayOf()
        val body = MultipartBody.create(MediaType.parse("image/*"), bytes)
        val image = MultipartBody.Part.createFormData("image", imageName, body)
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
}