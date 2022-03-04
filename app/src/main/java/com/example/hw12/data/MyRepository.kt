package com.example.hw12.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.hw12.model.User
import com.example.hw12.model.UserInfo
import com.github.leonardoxh.livedatacalladapter.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class MyRepository {
    private val userSource = UserDataSource()
    private val imageSource = ImageDataSource()

    private var putSucceed: MutableLiveData<Boolean>? = null
    private var getSucceed: MutableLiveData<Boolean>? = null

    private var id: LiveData<Resource<String>>? = null
    private var userResult: MutableLiveData<User>? = null
    private var userInfo: LiveData<Resource<UserInfo>>? = null
    private var image: LiveData<Resource<ResponseBody>>? = null

    private val setIdObserver = Observer<Resource<String>> {
        if (it.isSuccess) {
            if (image != null) {
                putResult(true)
            }
        } else {
            putResult(false)
        }
    }

    private val setImageObserver = Observer<Resource<ResponseBody>> {
        if (it.isSuccess) {
            if (id != null) {
                putResult(true)
            }
        } else {
            putResult(false)
        }
    }

    private val getImageObserver = Observer<Resource<ResponseBody>> {
        if (it.isSuccess) {
            if (userInfo != null) {
                createUser()
            }
        } else {
            getResult(false)
        }
    }

    private val getUserInfoObserver = Observer<Resource<UserInfo>> {
        if (it.isSuccess) {
            if (image != null) {
                createUser()
            }
        } else {
            getResult(false)
        }
    }

    private fun createUser() {
        val userInfo = userInfo!!
        val image = image!!
        userInfo.removeObserver(getUserInfoObserver)
        image.removeObserver(getImageObserver)
        userResult!!.postValue(User.fromUserInfo(userInfo.value!!.resource!!))

        getResult(true)
        this.userInfo = null
        this.image = null
    }

    private fun putResult(boolean: Boolean) {
        putSucceed?.postValue(boolean)
        putSucceed = null
    }

    private fun getResult(boolean: Boolean) {
        getSucceed?.postValue(boolean)
        getSucceed = null
    }

    fun putUser(userInfo: UserInfo, image: MultipartBody.Part):
            Pair<MutableLiveData<Boolean>, LiveData<Resource<String>>> {
        putSucceed = MutableLiveData()
        id = userSource.postUser(userInfo).also {
            it.observeForever(setIdObserver)
        }
        this.image = imageSource.postImage(userInfo.nationalCode, image).also {
            it.observeForever(setImageObserver)
        }
        return Pair(putSucceed!!, id!!)
    }

    fun getUser(id: String, email: String): Pair<MutableLiveData<Boolean>, MutableLiveData<User>> {
        userResult = MutableLiveData<User>()
        userInfo = userSource.getUser(id).also {
            it.observeForever(getUserInfoObserver)
        }
        this.image = imageSource.getImage(email).also {
            it.observeForever(getImageObserver)
        }
        return Pair(getSucceed!!, userResult!!)
    }
}
