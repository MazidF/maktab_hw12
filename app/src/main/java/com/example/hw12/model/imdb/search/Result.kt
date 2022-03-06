package com.example.hw12.model.imdb.search

import com.example.hw12.model.imdb.IMDBItemUiState

data class Result(
    val contentRating: Any,
    val description: String,
    val genreList: List<Genre>,
    val genres: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingVotes: String,
    val image: String,
    val metacriticRating: Any,
    val plot: String,
    val runtimeStr: String,
    val starList: List<Star>,
    val stars: String,
    val title: String
) {
    fun toItemUiState(onClick: IMDBItemUiState.() -> Unit) = IMDBItemUiState(title, image, id, onClick)
}