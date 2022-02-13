package com.example.hw12

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hw12.databinding.MovieBinding

open class MovieAdapter(private val model: NetflixViewModel, list: ArrayList<Movie>? = null) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private val list: ArrayList<Movie> = list ?: model.list

    class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: MovieBinding = DataBindingUtil.bind(view)!!

        fun autoFill(movie: Movie) {
            binding.movie = movie
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
        holder.autoFill(movie)
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
}
