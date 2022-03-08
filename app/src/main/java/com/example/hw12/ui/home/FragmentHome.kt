package com.example.hw12.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hw12.R
import com.example.hw12.databinding.FragmentHomeBinding
import com.example.hw12.isLiked
import com.example.hw12.model.imdb.IMDBItemUiState
import com.example.hw12.ui.MovieAdapter
import com.example.hw12.ui.MovieItemTouchHelper
import com.example.hw12.ui.NetflixViewModel
import com.example.hw12.ui.favorite.FragmentFavorite
import com.example.hw12.ui.favorite.FragmentFavoriteDirections
import com.example.hw12.ui.home.tracker.MovieSelectionTracker

class FragmentHome : Fragment(R.layout.fragment_home) {
    private val navController by lazy {
        findNavController()
    }
    private val model: NetflixViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding
    var state: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val adapter = MovieAdapter(model.list.value ?: arrayListOf()) { view, position, item ->
            view.setOnClickListener {
                isLiked(view, item.likeOrUnlike(position))
            }
            item
        }
        binding.homeList.apply {
            layoutManager = layoutManagerMaker()
            this.adapter = adapter
//            adapter.tracker = MovieSelectionTracker.getTracker(binding.homeList)
        }
        with(model) {
            val list = list.value
            if (list == null || list.isEmpty()) {
                this.list.observe(viewLifecycleOwner) {
                    if (it != null) {
                        adapter.addList(it)
                        this.list.removeObservers(viewLifecycleOwner)
                    }
                }
                loadMovies(this@FragmentHome::onItemClick)
            }
        }
    }

    private fun onItemClick(item: IMDBItemUiState) {
        val navDirection = when(parentFragmentManager.fragments.last()) {
            is FragmentHome -> FragmentHomeDirections.actionFragmentHomeToFragmentMovie(item)
            is FragmentFavorite -> FragmentFavoriteDirections.actionFragmentFavoriteToFragmentMovie(item)
            else -> {
                throw Exception("On Item Click in the wrong fragment")
            }
        }

        navController.navigate(navDirection)
    }

    private fun layoutManagerMaker(): GridLayoutManager {
        val context = requireContext()
        val spanCount =
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                3
            } else {
                6
            }
        return GridLayoutManager(requireContext(), spanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = 1
            }
        }
    }

    override fun onPause() {
        super.onPause()
        state = binding.homeList.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        binding.homeList.apply {
            layoutManager?.onRestoreInstanceState(state)
        }
    }

}
