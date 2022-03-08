package com.example.hw12.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView

class MovieItemTouchHelper(val recyclerView: RecyclerView, val itemChange: (Int) -> Boolean) : ItemTouchHelper.SimpleCallback(UP or DOWN, RIGHT) {
    private val adapter = recyclerView.adapter!!

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.absoluteAdapterPosition
        val to = target.absoluteAdapterPosition
        
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        if (itemChange(position)) {
            adapter.notifyItemRemoved(position)
        }
    }

    companion object {
        fun RecyclerView.connect(itemChange: (Int) -> Boolean) {
            val helper = MovieItemTouchHelper(this, itemChange)
            ItemTouchHelper(helper).attachToRecyclerView(this)
        }
    }
}