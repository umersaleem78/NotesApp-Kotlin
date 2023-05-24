package com.mus.mynotes.repository

import com.mus.mynotes.dal.local.TodoItemDAO
import com.mus.mynotes.models.todo.TodoItemMainModel
import javax.inject.Inject

class TodoItemRepository @Inject constructor(private val dao: TodoItemDAO) {

    fun fetchAllTodoItems(): List<TodoItemMainModel> {
        return dao.getAllTodoItems()
    }

    suspend fun addTodoItem(item: TodoItemMainModel): Long {
        return dao.insertTodoItem(item)
    }

    suspend fun updateTodoItem(item: TodoItemMainModel): Int {
        return dao.updateTodoItem(item)
    }

    suspend fun deleteTodoItem(item: Int): Int {
        return dao.deleteTodoItem(item)
    }

}