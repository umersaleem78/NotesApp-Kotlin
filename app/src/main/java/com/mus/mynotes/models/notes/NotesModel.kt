package com.mus.mynotes.models.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "notes")
data class NotesModel(

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

    @SerializedName("categoryId")
    @ColumnInfo(name = "categoryId")
    val categoryId: Int? = null,

    @SerializedName("categoryName")
    @ColumnInfo(name = "categoryName")
    val categoryName: String? = null,
):java.io.Serializable