package com.mus.mynotes.models.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class TodoItemModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String? = null,

    @SerializedName("isChecked")
    @ColumnInfo(name = "isChecked")
    var isChecked: Int? = null,

    @ColumnInfo(name = "isAddNewItem")
    var isAddNewItem: Int = 0

) : java.io.Serializable