package com.example.hw12.data

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.content.FileProvider
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
import com.example.hw12.model.imdb.trailer.TrailerResponse
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

    private var putResource: MutableLiveData<MyResource<String>>? = null
    private var getResource: MutableLiveData<MyResource<User>>? = null

    private var id: LiveData<Resource<String>>? = null
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
                getResult(true)
            }
        } else {
            getResult(false)
        }
    }

    private val getUserInfoObserver = Observer<Resource<UserInfo>> {
        if (it.isSuccess) {
            if (image != null) {
                getResult(true)
            }
        } else {
            getResult(false)
        }
    }

    private fun putResult(boolean: Boolean) {
        if (boolean) {
            putResource!!.postValue(MyResource.success(id!!.value!!.resource!!))
        } else {
            putResource!!.postValue(MyResource.error(Exception("Can not Load")))
        }
        putResource = null
        image = null
        id = null
    }

    private fun getResult(boolean: Boolean) {
        if (boolean) {
            val image = image!!
            val bytes = image.value!!.resource!!.bytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            getResource!!.postValue(MyResource.success(User.fromUserInfo(userInfo!!.value!!.resource!!).apply {
                this.image = bitmap
            }))
        } else {
            getResource!!.postValue(MyResource.error(Exception("Can not Load")))
        }
        getResource = null
        userInfo = null
        image = null
    }

    fun putUser(userInfo: UserInfo, image: MultipartBody.Part): LiveData<MyResource<String>> {
        putResource = MutableLiveData()
        id = userSource.postUser(userInfo).also {
            it.observeForever(setIdObserver)
        }
        this.image = imageSource.postImage(userInfo.nationalCode, image).also {
            it.observeForever(setImageObserver)
        }
        return putResource!!
    }

    fun getUser(id: String, email: String): LiveData<MyResource<User>> {
        getResource = MutableLiveData()
        userInfo = userSource.getUser(id).also {
            it.observeForever(getUserInfoObserver)
        }
        this.image = imageSource.getImage(email).also {
            it.observeForever(getImageObserver)
        }
        return getResource!!
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

    fun getTrailerUri(id: String, myCallback: MyCallback<TrailerResponse>? = null) {
        imdbSource.getTrailer(id, myCallback)
    }
}
