package com.example.hw12

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hw12.model.Movie
import com.example.hw12.utils.MyCallback
import okhttp3.*
import java.io.IOException

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

/*object HtmlValues {
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
}*/

@BindingAdapter("app:isLiked")
fun isLiked(imageView: ImageView, isLiked: Boolean) {
    imageView.apply {
        if (isLiked) {
            setColorFilter(Color.RED)
        } else {
            setColorFilter(Color.parseColor("#777575"))
        }
    }
}

@BindingAdapter("app:movie")
fun setImage(imageView: ImageView, movie: Movie) {
    movie.image?.let {
        imageView.setImageBitmap(it)
    } ?: imageView.setImageResource(R.drawable.icon_movie_default_white)
}


@BindingAdapter("app:image")
fun setImage(imageView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imageView.setImageBitmap(it)
    } ?: run {
        imageView.setImageResource(R.drawable.ic_profile)
    }
}

@BindingAdapter("app:isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("app:loadImage")
fun loadImage(imageView: ImageView, uri: String) {
    Glide.with(imageView.context)
        .load(uri)
        .apply(options)
        .into(imageView)
}

val options by lazy {
    RequestOptions()
        .centerCrop()
        .error(R.drawable.icon_movie_default_white)
        .placeholder(R.drawable.loading_animation)
}

fun <T> retrofit2.Call<T>.setCallback(myCallback: MyCallback<T>?) : T? {
    return if (myCallback != null) {
        myCallback.call(this)
        null
    } else {
        execute().body()
    }
}
