package com.example.hw12.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw12.ui.NetflixViewModel
import com.example.hw12.databinding.FragmentFavoriteBinding
import com.example.hw12.isLiked
import com.example.hw12.ui.MovieAdapter

class FragmentFavorite : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    private val model: NetflixViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val adapter = object : MovieAdapter(model.list.value!!, { view, position, item ->
            view.setOnClickListener {
                isLiked(view, item.likeOrUnlike(position))
            }
            item
        }) {
            private val favorites = model.favorites.value!!
            override fun onBindViewHolder(holder: MovieHolder, position: Int) {
                val realPosition = favorites[position]
                holder.bind(realPosition, list[realPosition])
            }

            override fun getItemCount() = favorites.size
        }
        with(binding) {
            favoriteList.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}
