package com.example.hw12.ui.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hw12.R

class FragmentMovie : Fragment() {
    var start = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (start) {
            findNavController().navigate(FragmentMovieDirections.actionFragmentMovieToFragmentPlayer(""))
        }
        start = false
    }
}
