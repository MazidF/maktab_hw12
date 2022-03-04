package com.example.hw12.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.hw12.model.Movie
import com.example.hw12.data.network.NetworkManager
import com.example.hw12.R
import com.example.hw12.model.Image
import com.example.hw12.viewmodel.NetflixViewModel
import com.example.hw12.viewmodel.NetflixViewModelFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class LoadingActivity : AppCompatActivity() {
    lateinit var model: NetflixViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this, NetflixViewModelFactory)
            .get(NetflixViewModel::class.java)
        setContentView(R.layout.activity_loading)
        if (model.list.isEmpty()) {
            loadMovies()
        } else {
            startActivity(MainActivity.getIntent(this@LoadingActivity))
            finish()
        }
    }

    private fun loadMovies() {
        val list = model.images
        if (list == null) {
            val request = Request.Builder()
                .url("https://picsum.photos/v2/list?limit=21")
                .build()

            NetworkManager.client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(
                            this@LoadingActivity,
                            "Failed to connect to server", Toast.LENGTH_SHORT
                        ).show()
                        loadMovies()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = response.body()?.string() ?: return
                    val listType = object : TypeToken<List<Image?>?>() {}.type
                    val gson = GsonBuilder().create()
                    val array: ArrayList<Image> = gson.fromJson(json, listType)
                    runOnUiThread {
                        model.images = array.map {
                            model.addMovie(Movie(it.author))
                            it.download_url
                        }
                    }
                    startActivity(MainActivity.getIntent(this@LoadingActivity))
                    finish()
                }
            })
        }
    }
}
