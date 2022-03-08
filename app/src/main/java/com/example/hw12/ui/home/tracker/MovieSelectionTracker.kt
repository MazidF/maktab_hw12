package com.example.hw12.ui.home.tracker

import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView

object MovieSelectionTracker {

    fun getTracker(recyclerView: RecyclerView) = SelectionTracker.Builder<Long>(
        "mySelection",
        recyclerView,
        StableIdKeyProvider(recyclerView),
        MovieItemDetailLookup(recyclerView),
        StorageStrategy.createLongStorage()
    ).withSelectionPredicate(
        SelectionPredicates.createSelectAnything() // or you can use Single type :)
    ).build()
}

