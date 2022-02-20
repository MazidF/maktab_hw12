package com.example.hw12

import android.app.ProgressDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.hw12.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    val model: NetflixViewModel by viewModels()
    private val controller by lazy {
        findNavController(R.id.container)
    }
    val dialog by lazy {
        ProgressDialog(this).apply {
            setTitle("Loading dada")
            setCancelable(false)
        }
    }
    lateinit var binding: ActivityMainBinding
    val client = OkHttpClient.Builder()
        .addInterceptor(getInterceptor())
        .connectTimeout(5, TimeUnit.SECONDS)
        .build()

    private fun getInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
/*        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                with(chain) {
                    val request = request()
                        .newBuilder()
                        .addHeader("token", "102345689")
                        .build()
                    return proceed(request)
                }
            }
        }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            model.isLoading.observe(this) {
                if (it.not()) {
                    dialog.cancel()
                }
            }
            loadMovies()
        }
        init()
    }

    private fun loadMovies() {
        val list = model.images
        if (list == null) {
            dialog.show()
            val request = Request.Builder()
                .url("https://picsum.photos/v2/list?limit=21")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // ignore
                    model.isLoading.postValue(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    model.isLoading.postValue(false)
                    val json = response.body()?.string() ?: return
                    val listType = object : TypeToken<ArrayList<Image?>?>() {}.type
                    val gson = GsonBuilder().create()
                    model.images = gson.fromJson(json, listType)
                }
            })
        }
    }

    private fun init() {
        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigation, controller)
            setSupportActionBar(toolbar)
        }
    }
}
