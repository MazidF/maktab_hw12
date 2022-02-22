package com.example.hw12

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hw12.databinding.FragmentHomeBinding

class FragmentHome : Fragment(R.layout.fragment_home) {
    private val model: NetflixViewModel by activityViewModels()
    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: MovieAdapter
    var state: Parcelable? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = MovieAdapter(model)
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.homeList.apply {
            layoutManager = layoutManagerMaker()
/*            if (adapter.itemCount == 0) {
                model.isLoading.observe(viewLifecycleOwner) {
                    if (it.not()) {
                        for (i in 0..20) {
                            adapter.addMovie(Movie("movie${i + 1}"))
                        }
                    }
                }
            }*/
            this.adapter = this@FragmentHome.adapter
            model.hasChanged.observe(viewLifecycleOwner) {
                if (it) {
//                    this@FragmentHome.adapter.changeList()
                    this.adapter = MovieAdapter(model)
                    Toast.makeText(context, "changed", Toast.LENGTH_SHORT).show()
                    model.hasChanged.value = false
                }
            }
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

    override fun onPause() {
        super.onPause()
        state = binding.homeList.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        binding.homeList.apply {
//            adapter = adapter
            layoutManager?.onRestoreInstanceState(state)
        }
    }

}
