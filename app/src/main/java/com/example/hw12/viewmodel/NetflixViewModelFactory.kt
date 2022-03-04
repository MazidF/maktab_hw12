package com.example.hw12.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object NetflixViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    val viewModel = super.create(NetflixViewModel::class.java)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == NetflixViewModel::class.java) {
            return viewModel as T
        }
        return super.create(modelClass)
    }

}
