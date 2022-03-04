package com.example.hw12.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.hw12.model.Movie
import com.example.hw12.data.network.NetworkManager.client
import com.example.hw12.data.network.NetworkManager.imageService
import com.example.hw12.R
import com.example.hw12.databinding.ActivityMainBinding
import com.example.hw12.model.Image
import com.example.hw12.model.PhotosItem
import com.example.hw12.viewmodel.NetflixViewModel
import com.example.hw12.viewmodel.NetflixViewModelFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    //    val model: NetflixViewModel by viewModels()
    lateinit var model: NetflixViewModel
    private val controller by lazy {
        findNavController(R.id.container)
    }

    /*    val dialog by lazy {
            ProgressDialog(this).apply {
                setTitle("Loading dada")
                setCancelable(false)
            }
        }*/
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this, NetflixViewModelFactory)
            .get(NetflixViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        if (savedInstanceState == null) {
//            model.isLoading.observe(this) {
//                if (it.not()) {
//                    dialog.cancel()
//                }
//            }
//            loadMovies()
//        }
        init()
    }

    private fun loadMovies() {
        val list = model.images
        if (list == null) {
/*            dialog.show()*/
            val request = Request.Builder()
                .url("https://picsum.photos/v2/list?limit=21")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // ignore
/*                    model.isLoading.postValue(false)*/
                }

                override fun onResponse(call: Call, response: Response) {
/*                    model.isLoading.postValue(false)*/
                    val json = response.body()?.string() ?: return
                    val listType = object : TypeToken<List<Image?>?>() {}.type
                    val gson = GsonBuilder().create()
                    val array: ArrayList<Image> = gson.fromJson(json, listType)
                    model.images = array.map {
                        it.download_url
                    }
                }
            })
        }
    }

    private fun loadImages() {
        val cal = imageService.getList(per_page = 99)
        cal.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                val json = response.body()?.string() ?: return
                val gson = GsonBuilder().create()
                val photosItem: PhotosItem = gson.fromJson(json, PhotosItem::class.java)
                Movie.reset()
                model.swap()
                model.images = photosItem.photos.photo.map {
                    model.addMovie(Movie(it.title.trim()))
                    it.url_s
                }
                model.swap()
                model.hasChanged.value = true
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Unable to load Images", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun init() {
        with(binding) {
            NavigationUI.setupWithNavController(bottomNavigation, controller)
            setSupportActionBar(toolbar)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("change list").setOnMenuItemClickListener {
            if (model.images2 == null) {
                loadImages()
            } else {
                model.hasChanged.value = true
            }
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
