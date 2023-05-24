package com.mus.mynotes.view.adapters.todo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mus.mynotes.databinding.ItemTodoChildListingBinding
import com.mus.mynotes.models.todo.TodoItemModel

class TodoChildItemListingAdapter(private val callBack: () -> Unit?) :
    RecyclerView.Adapter<TodoChildItemListingAdapter.ViewHolder>() {

    private val itemsList = ArrayList<TodoItemModel>()

    fun addItem(item: TodoItemModel) {
        itemsList.add(item)
        notifyItemInserted(itemsList.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: ArrayList<TodoItemModel>?) {
        if (items == null) {
            return
        }
        itemsList.clear()
        itemsList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTodoChildListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(private val binding: ItemTodoChildListingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(info: TodoItemModel) {
            binding.model = info
        }
    }
}