package com.mus.mynotes.view.adapters.todo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.mus.mynotes.R
import com.mus.mynotes.application.MyNotesApp.Companion.appContext
import com.mus.mynotes.constants.AppConstants
import com.mus.mynotes.databinding.ItemAddNewTodoBinding
import com.mus.mynotes.databinding.ItemTodoBinding
import com.mus.mynotes.models.todo.TodoItemModel
import com.mus.mynotes.utils.AppUtils

class AddTodoItemAdapter(private val callBack: (ArrayList<TodoItemModel>) -> Unit?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemsList = ArrayList<TodoItemModel>()

    fun addNewItem() {
        val model = TodoItemModel(
            title = "",
            isChecked = 0,
            isAddNewItem = AppConstants.TYPE_LIST_TODO_ITEM
        )
        itemsList.add(itemsList.size - 1, model)
        notifyItemInserted(itemsList.size - 1)
    }

    fun updateCheckBox(position: Int, isChecked: Boolean) {
        var isItemChecked = 0
        if (isChecked) {
            isItemChecked = 1
        }
        itemsList[position].isChecked = isItemChecked
        //notifyItemChanged(position)
        callBack.invoke(itemsList)
    }

    fun updateItem(
        position: Int,
        title: String,
        isChecked: Boolean,
        triggerNotifyItemChange: Boolean = false
    ) {
        var isItemChecked = 0
        if (isChecked) {
            isItemChecked = 1
        }
        val model = TodoItemModel(
            title = title,
            isChecked = isItemChecked,
            isAddNewItem = AppConstants.TYPE_LIST_TODO_ITEM
        )
        itemsList[position] = model
        if (triggerNotifyItemChange) {
            //notifyItemChanged(position)
        }
        callBack.invoke(itemsList)
    }

    fun removeItem(position: Int) {
        itemsList.removeAt(position)
        notifyItemRemoved(position)
        callBack.invoke(itemsList)
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

    fun fetchItemsList(): ArrayList<TodoItemModel> {
        return itemsList
    }

    override fun getItemViewType(position: Int): Int {
        if (itemsList[position].isAddNewItem == AppConstants.TYPE_ADD_NEW_TODO_ITEM) {
            return AppConstants.TYPE_ADD_NEW_TODO_ITEM
        }
        return AppConstants.TYPE_LIST_TODO_ITEM
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = LayoutInflater.from(parent.context)
        // Default case => add new item view holder
        var holder: RecyclerView.ViewHolder =
            AddNewItemViewHolder(ItemAddNewTodoBinding.inflate(context, parent, false))
        if (viewType == AppConstants.TYPE_LIST_TODO_ITEM) {
            holder =
                ListItemViewHolder(ItemTodoBinding.inflate(context, parent, false))
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsList[position]
        if (holder is AddNewItemViewHolder) {
            holder.bind(item)
        } else if (holder is ListItemViewHolder) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    inner class AddNewItemViewHolder(private val binding: ItemAddNewTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                addNewItem()
            }
        }

        fun bind(info: TodoItemModel) {
            // no binding required
        }
    }

    inner class ListItemViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivDelete.setOnClickListener {
                removeItem(absoluteAdapterPosition)
            }

            binding.cbSelection.setOnClickListener {
                updateCheckBox(absoluteAdapterPosition, binding.cbSelection.isChecked)
            }

            binding.etName.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    val name = binding.etName.text.toString()
                    if (name.isEmpty()) {
                        appContext?.let {
                            AppUtils.showToast(it.getString(R.string.enter_name))
                        }
                        return@setOnEditorActionListener false
                    }
                    updateItem(absoluteAdapterPosition, name, binding.cbSelection.isChecked, true)
                    addNewItem()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }

        fun bind(info: TodoItemModel) {
            info.title?.let {
                binding.etName.setText(it)
            }
            binding.cbSelection.isChecked = info.isChecked == 1

            binding.etName.addTextChangedListener {
                val inputStr = it?.toString() ?: ""
                updateItem(absoluteAdapterPosition, inputStr, binding.cbSelection.isChecked)
            }
        }
    }
}