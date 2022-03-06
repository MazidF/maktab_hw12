package com.example.hw12.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hw12.R
import com.example.hw12.databinding.MovieBinding
import com.example.hw12.model.imdb.IMDBItemUiState

open class MovieAdapter(val list: ArrayList<IMDBItemUiState>, val setup: (ImageView, Int, IMDBItemUiState) -> IMDBItemUiState)
    : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var binding: MovieBinding = DataBindingUtil.bind(view)!!

        fun bind(position: Int, item: IMDBItemUiState) {
            with(binding) {
                this.item = setup(movieLike, position, item)
                movieRoot.setOnClickListener {
                    item.invoke()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie, parent, false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(position, list[position])
    }

    override fun getItemCount() = list.size

    fun addList(list: List<IMDBItemUiState>) {
        this.list.addAll(list)
        notifyInsert(list.size)
    }

    fun notifyInsert(size: Int) {
        notifyItemRangeInserted(itemCount, size)
    }
}
