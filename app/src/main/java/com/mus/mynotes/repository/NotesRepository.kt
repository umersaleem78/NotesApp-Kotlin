package com.mus.mynotes.repository

import com.mus.mynotes.dal.local.NotesDAO
import com.mus.mynotes.models.notes.NotesModel
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesDAO: NotesDAO) {

    fun fetchNotesList(): List<NotesModel> {
        return notesDAO.getAllNotes()
    }

    suspend fun addNote(notesInfo: NotesModel): Long {
        return notesDAO.insertNote(notesInfo)
    }

    suspend fun updateNote(notesInfo: NotesModel) {
        notesDAO.updateNote(notesInfo)
    }

    suspend fun deleteNote(notesInfo: Int): Int {
        return notesDAO.deleteNote(notesInfo)
    }
}