package com.mus.mynotes.dal.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mus.mynotes.models.notes.NotesModel

@Dao
interface NotesDAO {

    @Insert
    suspend fun insertNote(notesInfo: NotesModel): Long

    @Update
    suspend fun updateNote(notesInfo: NotesModel)

    @Query("Delete FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Int): Int

    @Query("SELECT * from notes")
    fun observeNotesItems(): LiveData<List<NotesModel>>

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<NotesModel>

}