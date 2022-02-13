package com.example.hw12

import android.graphics.Color.RED
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.hw12.databinding.ActivityMainBinding
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    val model: NetflixViewModel by viewModels()
    private val controller by lazy {
        findNavController(R.id.container)
    }
    lateinit var binding: ActivityMainBinding
    lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            loadMovies()
        }
        init()
    }

    private fun loadMovies() {
    // TODO: set model list

    }

    private fun init() {
        client = OkHttpClient()
        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigation, controller)
            setSupportActionBar(toolbar)
        }
    }
}
