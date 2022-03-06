package com.example.hw12.model.imdb.properties

enum class IMDBGenres {
    SCI_FI {
        override val value = "sci_fi"
    }, ACTION {
        override val value = "action"
    }, ANIMATION {
        override val value = "animation"
    },  COMEDY {
        override val value = "comedy"
    }, ;
    abstract val value: String
    override fun toString() = value
}