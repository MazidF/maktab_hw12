package com.example.hw12

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData

data class Movie(val name: String, var image: Bitmap? = null) {

    companion object {
        private var counter = 1
        fun getId() = counter++

        fun reset() {
            counter = 1
        }
    }

    val id = getId()
    val isLiked by lazy {
        MutableLiveData(false)
    }
}
