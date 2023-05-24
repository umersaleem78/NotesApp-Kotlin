package com.mus.mynotes.view.adapters.todo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mus.mynotes.databinding.ItemTodoParentListingBinding
import com.mus.mynotes.models.todo.TodoItemMainModel

class TodoParentItemListingAdapter(private val callBack: (Boolean, Int, TodoItemMainModel) -> Unit?) :
    RecyclerView.Adapter<TodoParentItemListingAdapter.ViewHolder>() {

    private val itemsList = ArrayList<TodoItemMainModel>()

    fun addItem(item: TodoItemMainModel) {
        itemsList.add(item)
        notifyItemInserted(itemsList.size)
    }

    fun removeItem(position: Int?) {
        println("MainClass => position => $position")
        if (position !=null && position <= itemsList.size) {
            itemsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItem(position: Int?, item: TodoItemMainModel) {
        if (position != null && position < itemsList.size) {
            itemsList[position] = item
            notifyItemChanged(position)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: ArrayList<TodoItemMainModel>?) {
        if (items == null) {
            return
        }
        itemsList.clear()
        itemsList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTodoParentListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(private val binding: ItemTodoParentListingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivEdit.setOnClickListener {
                callBack.invoke(false, absoluteAdapterPosition, itemsList[absoluteAdapterPosition])
            }

            binding.ivDelete.setOnClickListener {
                callBack.invoke(true, absoluteAdapterPosition, itemsList[absoluteAdapterPosition])
            }
        }

        fun bind(info: TodoItemMainModel) {
            binding.model = info
            val adapter = TodoChildItemListingAdapter {

            }
            binding.rvChildItems.adapter = adapter
            adapter.addItems(info.item)
        }
    }
}