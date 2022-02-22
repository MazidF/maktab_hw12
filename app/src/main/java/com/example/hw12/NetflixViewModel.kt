package com.example.hw12

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NetflixViewModel : ViewModel() {
    val hasChanged by lazy {
        MutableLiveData<Boolean>()
    }
    val isLoading by lazy {
        MutableLiveData<Boolean>()
    }
    var images: List<String>? = null
    var images2: List<String>? = null
    var list = ArrayList<Movie>()
    var list2 = ArrayList<Movie>()
    var favorite = ArrayList<Int>()
    var favorite2 = ArrayList<Int>()
    var bitmap: Bitmap? = null

    init {
        hasChanged.observeForever {
            if (it) {
                swap()
            }
        }
    }

    fun swap() {
        val tempList = list
        list = list2
        list2 = tempList

        val temp = favorite
        favorite = favorite2
        favorite2 = temp

        val tempImages = images
        images = images2
        images2 = tempImages
    }

    fun addFavorite(movie: Movie) {
        favorite.add(movie.id - 1)
    }

    fun removeFavorite(movie: Movie) {
        favorite.remove(movie.id - 1)
    }

    fun addMovie(movie: Movie) {
        movie.isLiked.observeForever {
            if (it) {
                addFavorite(movie)
            } else {
                removeFavorite(movie)
            }
        }
        list.add(movie)
    }

    fun addMovies(vararg movies: Movie) {
        for (movie in movies) {
            addMovie(movie)
        }
    }
}
