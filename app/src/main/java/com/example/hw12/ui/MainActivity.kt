package com.example.hw12.ui

import android.os.Bundle
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
                    isLoading.removeObservers(this@MainActivity)
                }
            }
            hide.observe(this@MainActivity) {
                binding.bottomNavigation.isVisible = it == false
            }
        }
        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigation, controller)
            setSupportActionBar(toolbar)
        }
    }
}
