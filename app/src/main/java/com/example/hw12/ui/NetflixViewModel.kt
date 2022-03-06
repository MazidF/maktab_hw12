package com.example.hw12.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hw12.data.MyRepository
import com.example.hw12.model.imdb.IMDBItemUiState
import com.example.hw12.model.imdb.search.SearchResponse
import com.example.hw12.utils.MyCallback

class NetflixViewModel : ViewModel() {
    private val repository = MyRepository

    val list = MutableLiveData<ArrayList<IMDBItemUiState>>()
    val failed by lazy {
        MutableLiveData(false)
    }
    val favorites by lazy {
        MutableLiveData(ArrayList<Int>(5))
    }
    val isLoading = MutableLiveData<Boolean>()

    fun loadMovies(onClick: IMDBItemUiState.() -> Unit) {
        isLoading.value = true
        val callback = object : MyCallback<SearchResponse> {
            override fun onFailure(t: Throwable) {
                failed.value = true
                isLoading.value = false
            }

            override fun onResponse(t: SearchResponse?) {
                isLoading.value = false
                if (t == null) {
                    failed.value = true
                    return
                }
                val resultList = t.results.map {
                    val item = it.toItemUiState(onClick)
                    item.apply {
                        isLiked.observeForever { bool ->
                            favorite(item.position, bool)
                        }
                    }
                }
                list.value = ArrayList(resultList)
            }
        }

        repository.getMovieList(callback)
    }

    fun favorite(position: Int, isLiked: Boolean) {
        if (isLiked) {
            favorites.value!!.add(position)
        } else {
            favorites.value!!.remove(position)
        }
    }
}
