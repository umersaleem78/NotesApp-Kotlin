package com.mus.mynotes.models.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "todo")
data class TodoItemMainModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String? = null,

    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String? = null,

    @SerializedName("timestamp")
    @ColumnInfo(name = "timestamp")
    val timestamp: String? = null,

    @SerializedName("color")
    @ColumnInfo(name = "color")
    val color: String? = null,

    @ColumnInfo(name = "item")
    var item: ArrayList<TodoItemModel>?
) : java.io.Serializable