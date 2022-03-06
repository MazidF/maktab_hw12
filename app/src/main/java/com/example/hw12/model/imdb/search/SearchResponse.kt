package com.example.hw12.model.imdb.search

data class SearchResponse(
    val errorMessage: Any,
    val queryString: String,
    val results: List<Result>
)