package com.mus.mynotes.dal.local

import androidx.room.*
import com.mus.mynotes.models.todo.TodoItemMainModel

@Dao
interface TodoItemDAO {

    @Insert
    suspend fun insertTodoItem(item: TodoItemMainModel): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodoItem(item: TodoItemMainModel): Int

    @Query("Delete FROM todo WHERE id = :id")
    suspend fun deleteTodoItem(id: Int): Int

    @Query("SELECT * FROM todo")
    fun getAllTodoItems(): List<TodoItemMainModel>

}