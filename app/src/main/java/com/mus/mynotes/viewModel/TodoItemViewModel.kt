package com.mus.mynotes.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mus.mynotes.R
import com.mus.mynotes.application.ResourceProvider
import com.mus.mynotes.constants.AppConstants
import com.mus.mynotes.models.todo.TodoItemMainModel
import com.mus.mynotes.models.todo.TodoItemModel
import com.mus.mynotes.repository.TodoItemRepository
import com.mus.mynotes.utils.AppUtils
import com.mus.mynotes.utils.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val repository: TodoItemRepository
) : ViewModel() {

    var title: String? = null
    var description: String? = null
    var color: String? = null
    var isUpdateRequest: Boolean = false
    var model: TodoItemMainModel? = null
    var todoItemsList = ArrayList<TodoItemModel>()

    var failureMessage: MutableLiveData<String> = MutableLiveData()
    var success: MutableLiveData<TodoItemMainModel> = MutableLiveData()
    var removeItem: MutableLiveData<Int?> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()
    var loader: MutableLiveData<Boolean> = MutableLiveData()
    var todoItemsListResponse: MutableLiveData<ArrayList<TodoItemMainModel>> = MutableLiveData()


    fun fetchAddNewItem(): TodoItemModel {
        return TodoItemModel(isAddNewItem = AppConstants.TYPE_ADD_NEW_TODO_ITEM)
    }

    fun fetchTodoMainItemModel(): TodoItemMainModel {
        if (!todoItemsList.contains(fetchAddNewItem())) {
            todoItemsList.add(fetchAddNewItem())
        }
        return TodoItemMainModel(
            id = model?.id,
            title = title,
            description = description,
            timestamp = DateTimeUtils.getTimeStamp(),
            color = color ?: AppUtils.getRandomColor(),
            item = todoItemsList
        )
    }

    fun addOrUpdateItem() = viewModelScope.launch {
        if (validateTitle()) {
            val model = fetchTodoMainItemModel()
            val childItems = model.item
            if (childItems?.isNotEmpty() == true && childItems.contains(fetchAddNewItem())) {
                childItems.remove(fetchAddNewItem())
            }
            model.item = childItems
            if (isUpdateRequest) {
                repository.updateTodoItem(model)
            } else {
                val insertedId = repository.addTodoItem(model)
                model.id = insertedId.toInt()
            }
            success.postValue(model)
        }
    }

    private fun validateTitle(): Boolean {
        if (title.isNullOrEmpty()) {
            failureMessage.postValue(resourceProvider.getString(R.string.enter_title))
            return false
        }
        return true
    }


    fun fetchTodoItemsList() = viewModelScope.launch {
        loader.postValue(true)
        val response = repository.fetchAllTodoItems()
        Handler(Looper.getMainLooper()).postDelayed({
            loader.postValue(false)
            todoItemsListResponse.postValue(response as ArrayList<TodoItemMainModel>?)
        }, 500)
    }

    fun deleteTodoItem(id: Int?) = viewModelScope.launch {
        if (id == null) {
            message.postValue(resourceProvider.getString(R.string.invalid_id_provided))
            return@launch
        }
        val response = repository.deleteTodoItem(id)
        if (response == 0) {
            message.postValue(resourceProvider.getString(R.string.unable_to_delete_note))
        } else {
            removeItem.postValue(id)
        }
    }
}