package com.example.hw12.model.imdb

data class IMDBResponse(
    val errorMessage: String,
    val items: List<Item>
)