package com.example.hw12.model.imdb.properties

enum class IMDBTitleType {
    VIDEO {
        override val value = "video"
    }, TV_MOVIE {
        override val value = "tv_movie"
    }, TV_SERIES {
        override val value = "tv_series"
    }, ;
    abstract val value: String
    override fun toString() = value
}
