package com.example.hw12.model.imdb.properties

enum class SearchMethod {
    TOP250MOVIES {
        override val value = "Top250Movies"
    }, TOP250TVS {
        override val value = "Top250TVs"
    }, COMING_SOON {
        override val value = "ComingSoon"
    } ;
    abstract val value: String
    override fun toString() = value
}
