package com.example.hw12.model.imdb

data class Item(
    val crew: String,
    val fullTitle: String,
    val id: String,
    val imDbRating: String,
    val imDbRatingCount: String,
    val image: String,
    val rank: String,
    val title: String,
    val year: String
) {
    fun toItemUiState(onClick: IMDBItemUiState.() -> Unit) = IMDBItemUiState(title, image, id, onClick)
}

