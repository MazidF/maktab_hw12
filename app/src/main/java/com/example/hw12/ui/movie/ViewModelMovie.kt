package com.example.hw12.ui.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw12.data.MyRepository
import com.example.hw12.model.imdb.trailer.TrailerResponse
import com.example.hw12.utils.MyCallback

class ViewModelMovie : ViewModel() {
    private val repository = MyRepository
    val failed by lazy {
        MutableLiveData<Boolean>()
    }
    val uri by lazy {
        MutableLiveData<String>()
    }

    fun getTrailerUri(id: String) {
        val callback = object : MyCallback<TrailerResponse> {
            override fun onFailure(t: Throwable) {
                failed.value = true
            }

            override fun onResponse(t: TrailerResponse?) {
                if (t == null) {
                    failed.value = true
                    return
                }
                uri.value = t.link
            }
        }
        repository.getTrailerUri(id, callback)
    }
}
