package com.mus.mynotes.view.adapters.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mus.mynotes.databinding.ItemNotesBinding
import com.mus.mynotes.models.notes.NotesModel

class NotesListingAdapter(private val callBack: (Boolean, Int, NotesModel?) -> Unit?) :
    RecyclerView.Adapter<NotesListingAdapter.ViewHolder>() {

    private val itemsList = ArrayList<NotesModel>()

    fun addItem(item: NotesModel) {
        itemsList.add(item)
        notifyItemInserted(itemsList.size)
    }

    fun updateItem(position: Int?, item: NotesModel) {
        if (position != null && position < itemsList.size) {
            itemsList[position] = item
            notifyItemChanged(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: ArrayList<NotesModel>?) {
        if (items == null) {
            return
        }
        itemsList.clear()
        itemsList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemsList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class ViewHolder(private val binding: ItemNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivDelete.setOnClickListener {
                callBack.invoke(true, absoluteAdapterPosition, itemsList[absoluteAdapterPosition])
            }

            itemView.setOnClickListener {
                callBack.invoke(false, absoluteAdapterPosition, itemsList[absoluteAdapterPosition])
            }
        }

        fun bind(info: NotesModel) {
            binding.model = info
        }
    }
}