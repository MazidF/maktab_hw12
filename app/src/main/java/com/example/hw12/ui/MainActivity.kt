package com.example.hw12.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.hw12.R
import com.example.hw12.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    val model: NetflixViewModel by viewModels()
    private val controller by lazy {
        findNavController(R.id.container)
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        with(model) {
            isLoading.observe(this@MainActivity) {
                if (it == false) {
                    binding.mainList.removeViewAt(0)
                    isLoading.value = null
                    isLoading.removeObservers(this@MainActivity)
                }
            }
        }
        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigation, controller)
            setSupportActionBar(toolbar)
        }
    }
}
