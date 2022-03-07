package com.example.hw12.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.hw12.data.local.SaveLocalDataSource
import com.example.hw12.data.remote.IMDBDataSource
import com.example.hw12.data.remote.UserDataSource
import com.example.hw12.model.user.User
import com.example.hw12.model.user.UserInfo
import com.example.hw12.model.imdb.IMDBResponse
import com.example.hw12.model.imdb.properties.SearchMethod
import com.example.hw12.model.imdb.search.SearchResponse
import com.example.hw12.model.user.UserResponse
import com.example.hw12.model.user.UsersResponse
import com.example.hw12.utils.MyCallback
import com.github.leonardoxh.livedatacalladapter.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.Serializable

object MyRepository {
    private val saver = SaveLocalDataSource()
    private val imdbSource = IMDBDataSource()
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

    fun getUsers(): android.arch.lifecycle.LiveData<Resource<UsersResponse>> {
        return userSource.getUsers()
    }

    //////////////////////////////////////////////////////////////////////////////

    fun save(serializable: Serializable, context: Context): Boolean {
        return saver.save(serializable, context)
    }

    fun <T : Serializable> load(context: Context) : T? {
        return saver.load(context)
    }

    //////////////////////////////////////////////////////////////////////////////

    fun getMovieList(callback: MyCallback<SearchResponse>) {
        imdbSource.advanceSearch(callback)
    }

    fun getComingSoonList(callback: MyCallback<IMDBResponse>) {
        imdbSource.search(SearchMethod.COMING_SOON, callback)
    }
}
