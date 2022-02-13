package com.example.hw12

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.hw12.HtmlValues.image
import com.example.hw12.HtmlValues.movie
import com.example.hw12.HtmlValues.name
import com.example.hw12.HtmlValues.section
import okhttp3.*
import java.io.IOException
import java.util.regex.Pattern

fun OkHttpClient.requestMaker(url: String): Request {
    return Request.Builder()
        .url(url)
        .build()
}

fun OkHttpClient.requestExecute(url: String): Response {
    val request = requestMaker(url)
    return this.newCall(request).execute()
}

fun OkHttpClient.requestEnqueue(
    url: String,
    fail: ((Call) -> Unit)? = null,
    success: (Call, Response) -> Unit
) {
    val request = requestMaker(url)
    this.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) = fail?.invoke(call) ?: Unit

        override fun onResponse(call: Call, response: Response) = success(call, response)
    })
}

fun OkHttpClient.request3(url: String): Response {
    val request = requestMaker(url)
    return this.newCall(request).execute()
}

object HtmlValues {
    const val PAGE = "nm-collections-page"
    const val CONTAINER = "nm-collections-container with-blur"
    const val ROW = "nm-collections-row"
    const val LIST = "nm-content-horizontal-row-item-container"
    const val ITEM = "nm-content-horizontal-row-item"
    const val TITLE = "nm-collections-title nm-collections-link"
    const val IMAGE = "nm-collections-title-img"
    const val NAME = "nm-collections-title-name"
    val section by lazy {
        Pattern.compile("<section class=\"nm-collections-row\" data-uia=\"collections-row\">.+</section>")
    }
    val movie by lazy {
        Pattern.compile("<li class=\"nm-content-horizontal-row-item\" data-uia=\"collections-row-item\">.+</li>")
    }
    val image by lazy {
        Pattern.compile("<span class=\"nm-collections-title-name\">.*</span>")
    }
    val name by lazy {
        Pattern.compile("<span class=\"nm-collections-title-name\">.*</span>")
    }
}

fun parser(response: Response) {
    val code = response.code()
    if (code !in 200 until 300) return
    val body = response.body()?.string() ?: return
    val matcher = section.matcher(body)
    if (matcher.find()) {
        getMovies(matcher.group())
    }
}

fun getMovies(input: String): ArrayList<Movie> {
    val list = ArrayList<Movie>(40)
    var index = 0
    var nameMovie = ""
    var imageMovie = ""
    var group: String
    val matcher = movie.matcher(input)
    while (matcher.find(index)) {
        group = matcher.group()
        image.matcher(group).run {
            if (find()) {
                imageMovie = group()
            }
        }
        name.matcher(group).run {
            if (find()) {
                nameMovie = group()
            }
        }
//        list.add(Movie(nameMovie, imageMovie))
        Log.d("index_", index.toString())
        index = matcher.end()
    }
    return list
}

@BindingAdapter("app:isLiked")
fun isLiked(imageView: ImageView, movie: Movie) {
    imageView.apply {
        if (movie.isLiked) {
            setColorFilter(Color.RED)
        } else {
            clearColorFilter()
        }
    }
}

@BindingAdapter("app:image")
fun setImage(imageView: ImageView, movie: Movie) {
    movie.image?.let {
        imageView.setImageBitmap(it)
    } ?: imageView.setImageResource(R.drawable.icon_movie_default)
}
