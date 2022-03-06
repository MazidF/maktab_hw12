package com.example.hw12.model.imdb

import androidx.lifecycle.MutableLiveData
import java.io.Serializable

class IMDBItemUiState(
    val title: String,
    val image: String,
    val id: String,
    val onClick: IMDBItemUiState.() -> Unit
) : Serializable {
    var position = -1
    val isLiked by lazy {
        MutableLiveData(false)
    }

    fun likeOrUnlike(position: Int): Boolean {
        this.position = position
        isLiked.run {
            value = value!!.not()
            return value!!
        }
    }

    fun invoke() {
        onClick(this)
    }
}
