package com.example.hw12

import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw12.databinding.FragmentFavoriteBinding

class FragmentFavorite : Fragment() {
    private lateinit var adapter: MovieAdapter
    private val model: NetflixViewModel by activityViewModels()
    lateinit var binding: FragmentFavoriteBinding

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
        model.hasChanged.observe(viewLifecycleOwner) { hasChanged ->
            if (hasChanged) {
//                adapter.changeList(ArrayList(model.favorite.map { model.list[it] }))
                val favorites = model.favorite.map { model.list[it] }
                this.adapter = MovieAdapter(model, ArrayList(favorites))
                binding.favoriteList.adapter = this.adapter
            }
        }
        val favorites = model.favorite.map { model.list[it] }
        adapter = MovieAdapter(model, ArrayList(favorites))
        binding.favoriteList.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@FragmentFavorite.adapter
        }
    }
}
