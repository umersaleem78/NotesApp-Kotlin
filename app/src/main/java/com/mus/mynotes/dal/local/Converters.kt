package com.mus.mynotes.dal.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mus.mynotes.models.todo.TodoItemModel
import java.lang.reflect.Type


// for handling array list etc in room entities
class Converters {
    @TypeConverter
    fun fromTodoItemModel(value: String?): ArrayList<TodoItemModel>? {
        if(value.isNullOrEmpty() || value == "null"){
            return arrayListOf()
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<TodoItemModel?>?>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toTodoItemModel(list: ArrayList<TodoItemModel?>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type =
            object : TypeToken<List<TodoItemModel?>?>() {}.type
        return gson.toJson(list, type)
    }
}