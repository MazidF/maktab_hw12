package com.example.hw12.model.imdb.properties

enum class IMDBLanguages {
    ENGLISH {
        override val value = "en"
    }, PERSIAN {
        override val value = "fa"
    }, ;

    abstract val value: String
    override fun toString() = value
}
