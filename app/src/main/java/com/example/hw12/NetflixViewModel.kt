package com.example.hw12

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

typealias LiveMovie = LiveData<Movie>

class NetflixViewModel : ViewModel() {
    val list = ArrayList<LiveMovie>()
    val favorite by lazy { // index of movies in list
        ArrayList<Int>()
    }

    fun addFavorite(movie: Movie) {
        movie.isLiked = true
        favorite.add(movie.id - 1)
    }

    fun addMovie(movie: Movie) {
        list.add(MutableLiveData(movie))
    }
}
