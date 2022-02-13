package com.example.hw12

import androidx.lifecycle.ViewModel

class NetflixViewModel : ViewModel() {
    val list = ArrayList<Movie>()
    val favorite by lazy { // index of movies in list
        ArrayList<Int>()
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
