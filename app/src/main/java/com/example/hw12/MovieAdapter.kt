package com.example.hw12

import android.graphics.Color.RED
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.hw12.databinding.MovieBinding

class MovieAdapter(private val list: ArrayList<LiveMovie>) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
/*        val liveMovie by lazy {
            MutableLiveData<Movie>()
        }*/
        var binding: MovieBinding = DataBindingUtil.bind(view)!!
        val image = binding.movieImage
        val like = binding.movieLike
        val name = binding.movieName
//        var image: ImageView = view.findViewById(R.id.movie_image)
//        var like: ImageView = view.findViewById(R.id.movie_like)
//        var name: TextView = view.findViewById(R.id.movie_name)

        fun fill(movie: Movie) {
            name.text = movie.name
            movie.image?.let {
                image.setImageBitmap(it)
            } ?: image.setImageResource(R.drawable.movie_spider_man)
            like.setOnClickListener {
                (it as ImageView).run {
                    if (tag == true) {
                        setColorFilter(RED)
                    } else {
                        clearColorFilter()
                    }
                }
            }
        }

/*        fun autoFill(movie: Movie) {
            liveMovie.value = movie
            binding.movie = liveMovie
        }*/

        fun autoFill(liveMovie: LiveMovie) {
            binding.movie = liveMovie
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

    fun addMovie(vararg movies: Movie) {
        list.addAll(movies.map {
            MutableLiveData(it)
        })
        notifyItemRangeInserted(0, movies.size)
    }

    fun addMovie(movie: Movie, position: Int = itemCount) {
        list.add(MutableLiveData(movie))
        notifyItemInserted(position)
    }

    fun remove(movie: Movie) {
        remove(movie.id)
    }

    private fun remove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}
