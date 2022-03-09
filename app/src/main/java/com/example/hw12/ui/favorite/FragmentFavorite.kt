package com.example.hw12.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw12.ui.NetflixViewModel
import com.example.hw12.databinding.FragmentFavoriteBinding
import com.example.hw12.isLiked
import com.example.hw12.model.imdb.IMDBItemUiState
import com.example.hw12.ui.MovieAdapter
import com.example.hw12.ui.MovieItemTouchHelper.Companion.connect

class FragmentFavorite : Fragment() {
    val navController by lazy {
        findNavController()
    }
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
        val favorites = model.favorites.value!!
        val modelList = model.list.value!!
        val adapter = object : MovieAdapter(model.list.value!!, setup = { _, _, item ->
            item
        }) {
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
                connect {
                    val realPosition = favorites[it]
                    modelList[realPosition].likeOrUnlike(realPosition)
                    true
                }
            }
        }
        with(model) {
            clickedItem.observe(viewLifecycleOwner) {
                if (it != null) {
                    clickedItem.value = null
                    onItemClick(it)
                }
            }
        }
    }

    private fun onItemClick(item: IMDBItemUiState) {
        navController.navigate(FragmentFavoriteDirections.actionFragmentFavoriteToFragmentMovie(item))
    }
}
