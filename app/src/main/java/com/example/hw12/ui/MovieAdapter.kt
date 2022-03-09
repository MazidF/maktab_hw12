package com.example.hw12.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.*
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.hw12.R
import com.example.hw12.databinding.MovieBinding
import com.example.hw12.model.imdb.IMDBItemUiState

open class MovieAdapter(val list: ArrayList<IMDBItemUiState>, hasTracker: Boolean = false, val setup: (ImageView, Int, IMDBItemUiState) -> IMDBItemUiState)
    : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    private val hasSelection by lazy {
        MutableLiveData(false)
    }

    fun addObserver(lifecycleObserver: LifecycleOwner, observer: Observer<Boolean>) {
        hasSelection.observe(lifecycleObserver, observer)
    }

    init {
        setHasStableIds(hasTracker)
    }

    var tracker: SelectionTracker<Long>? = null
    set(value) {
        value?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                hasSelection.value = value.hasSelection()
            }
        })
        field = value
    }

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var binding = MovieBinding.bind(view).apply {
            this@MovieAdapter.hasSelection.observeForever {
                this.hasSelection = it
                this.executePendingBindings()
            }

        }

        fun bind(position: Int, item: IMDBItemUiState) {
            with(binding) {
                this.item = setup(movieLike, position, item)
                movieCheckBtn.isChecked = tracker?.isSelected(position.toLong()) ?: false
                movieRoot.setOnClickListener {
                    item.invoke()
                }
            }
        }

        fun getItemDetail() = object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition() = absoluteAdapterPosition
            override fun getSelectionKey() = itemId
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun notifyInsert(size: Int) {
        notifyItemRangeInserted(itemCount, size)
    }
}
