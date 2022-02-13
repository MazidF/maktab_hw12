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
    private val model: NetflixViewModel by activityViewModels()
    var state: Parcelable? = null
    lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        val favorites = model.favorite.map { model.list[it] }
        val adapter = MovieAdapter(model, ArrayList(favorites))
        binding.favoriteList.apply {
//            layoutManager = LinearLayoutManager(requireContext()).apply {
//                this@FragmentFavorite.state?.let {
//                    onRestoreInstanceState(it)
//                }
//            }
            layoutManager = GridLayoutManager(requireContext(), 1).apply {
                spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int) = 1
                }
            }
            this.adapter = adapter
        }
        return binding.root
    }
}
