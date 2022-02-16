package com.example.hw12

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hw12.databinding.FragmentHomeBinding

class FragmentHome : Fragment(R.layout.fragment_home) {
    private val model: NetflixViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeList.apply {
            layoutManager = layoutManagerMaker()
            val adapter = MovieAdapter(model)
            if (adapter.itemCount == 0) {
                model.isLoading.observe(viewLifecycleOwner) {
                    if (it.not()) {
                        for (i in 0..20) {
                            adapter.addMovie(Movie("movie${i + 1}"))
                        }
                    }
                }
            }
            this.adapter = adapter
        }
        return binding.root
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

}
