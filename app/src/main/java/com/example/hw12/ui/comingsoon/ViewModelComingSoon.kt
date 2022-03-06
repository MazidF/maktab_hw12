package com.example.hw12.ui.comingsoon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw12.data.MyRepository
import com.example.hw12.model.imdb.IMDBResponse
import com.example.hw12.model.imdb.IMDBItemUiState
import com.example.hw12.utils.MyCallback

class ViewModelComingSoon : ViewModel() {
    val failed by lazy {
        MutableLiveData(false)
    }
    val list by lazy {
        MutableLiveData<List<IMDBItemUiState>>()
    }
    private val repository = MyRepository

    fun loadComingSoon(onclick: (IMDBItemUiState) -> Unit) {
        failed.value = false
        val callback = object : MyCallback<IMDBResponse> {
            override fun onFailure(t: Throwable) {
                failed.value = true
            }

            override fun onResponse(t: IMDBResponse?) {
                if (t == null) {
                    failed.value = true
                    return
                }
                list.value = t.items.map {
                    it.toItemUiState(onclick)
                }
            }
        }
        repository.getComingSoonList(callback)
    }
}
