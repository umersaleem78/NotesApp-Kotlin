package com.mus.mynotes.dal.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mus.mynotes.models.notes.NotesModel
import com.mus.mynotes.models.todo.TodoItemMainModel

@Database(entities = [NotesModel::class, TodoItemMainModel::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDAO
    abstract fun todoDao(): TodoItemDAO
}