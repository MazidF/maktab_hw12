package com.example.hw12

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.hw12.databinding.ActivityMainBinding
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    private val controller by lazy {
        findNavController(R.id.container)
    }
    lateinit var binding: ActivityMainBinding
    lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        client = OkHttpClient()
        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigation, controller)
        }
    }
}
