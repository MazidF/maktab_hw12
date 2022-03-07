package com.example.hw12.ui.signin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hw12.R
import com.example.hw12.databinding.UserResponseBinding
import com.example.hw12.model.user.UserResponse
import com.example.hw12.model.user.UsersResponse
import com.example.hw12.model.user.UsersResponseItem

class UserAdapter(val list: ArrayList<UsersResponseItem>, val onClick: (UsersResponseItem) -> Unit) : RecyclerView.Adapter<UserAdapter.MyHolder>() {

    inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = UserResponseBinding.bind(view)

        fun bind(item: UsersResponseItem) {
            with(binding) {
                user = item
                root.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_response, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addList(list: UsersResponse) {
        this.list.addAll(list)
        notifyItemRangeInserted(itemCount - list.size, list.size)
    }
}
