package com.example.hw12

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NetflixViewModel : ViewModel() {
    val isLoading by lazy {
        MutableLiveData<Boolean>()
    }
    var images: ArrayList<Image>? = null
    var list = ArrayList<Movie>()
    val favorite by lazy { // index of movies in list
        ArrayList<Int>()
    }
    var bitmap: Bitmap? = null

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
