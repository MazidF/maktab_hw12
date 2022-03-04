package com.example.hw12.ui

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.hw12.R
import com.example.hw12.databinding.MovieBinding
import com.example.hw12.model.Movie
import com.example.hw12.viewmodel.NetflixViewModel

val options by lazy {
    RequestOptions()
        .centerCrop()
        .error(R.drawable.icon_movie_default_white)
        .placeholder(R.drawable.loading_animation)
}

open class MovieAdapter(private val model: NetflixViewModel, list: ArrayList<Movie>? = null)
    : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private var list: ArrayList<Movie> = list ?: model.list

    class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: MovieBinding = DataBindingUtil.bind(view)!!
        val contex = view.context

        fun autoFill(movie: Movie, url: String? = null) {
            binding.movie = movie
            if (url != null) {
                Glide.with(contex)
                    .asBitmap()
                    .load(url)
                    .apply(options)
                    .addListener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            movie.image = resource
                            return false
                        }

                    })
                    .into(binding.movieImage)
            }
            binding.movieLike.setOnClickListener {
                binding.movie = movie.apply {
                    isLiked.value = isLiked.value!!.not()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = list[position]
        var url: String? = null
        if (movie.image == null) {
            url = model.images?.get(position)
        }
        holder.autoFill(movie, url)
    }

    override fun getItemCount() = list.size

    fun addMovie(movie: Movie, position: Int = itemCount) {
        model.addMovie(movie)
        notifyItemInserted(position)
    }

    fun addMovies(vararg movies: Movie) {
        model.addMovies(*movies)
        notifyItemRangeInserted(itemCount, movies.size)
    }

    private fun remove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun remove(movie: Movie) {
        remove(movie.id)
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun changeList(newList: ArrayList<Movie>? = null) {
//        this.list = newList ?: model.list
//        notifyDataSetChanged()
//    }
}
