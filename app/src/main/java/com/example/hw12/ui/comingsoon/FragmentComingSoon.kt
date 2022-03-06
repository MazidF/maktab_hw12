package com.example.hw12.ui.comingsoon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw12.R
import com.example.hw12.databinding.FragmentComingSoonBinding
import com.example.hw12.model.imdb.IMDBItemUiState
import com.example.hw12.ui.MovieAdapter

class FragmentComingSoon : Fragment() {
    private val navController by lazy {
        findNavController()
    }
    private val model: ViewModelComingSoon by viewModels()
    private lateinit var binding: FragmentComingSoonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_coming_soon, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val adapter = MovieAdapter(ArrayList(model.list.value ?: listOf())) { _, _, item ->
            item.apply {
                this.isLiked.value = null
            }
        }
        with(model) {
            list.observe(viewLifecycleOwner) {
                if (it != null && it.isNotEmpty()) {
                    adapter.addList(it)
                    list.value = null
                }
            }
            val list = list.value
            if (list == null || list.isEmpty()) {
                loadComingSoon(this@FragmentComingSoon::openMovieFragment)
            }
        }
        with(binding) {
            this.isFailed = model.failed
            this.lifecycleOwner = viewLifecycleOwner

            comingSoonList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter = adapter
            }
        }
    }

    private fun openMovieFragment(item: IMDBItemUiState) {
        navController.navigate(FragmentComingSoonDirections.actionFragmentComingSoonToFragmentMovie(item))
    }
}
