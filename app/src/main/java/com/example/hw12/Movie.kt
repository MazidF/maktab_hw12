package com.example.hw12

import android.graphics.Bitmap

data class Movie(val name: String, val image: Bitmap? = null) {

    companion object {
        private var counter = 1
        fun getId() = counter++
    }

    val id = getId()
    var isLiked = false
}
