package com.mus.mynotes.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mus.mynotes.R
import com.mus.mynotes.application.ResourceProvider
import com.mus.mynotes.models.notes.NotesModel
import com.mus.mynotes.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {


    var noNotesFound: MutableLiveData<Boolean> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()
    var notesList: MutableLiveData<ArrayList<NotesModel>> = MutableLiveData()


    fun fetchAllNotes() = viewModelScope.launch {
        val response = repository.fetchNotesList()
        noNotesFound.postValue(response.isEmpty())
        notesList.postValue(response as ArrayList<NotesModel>?)
    }

    fun deleteNote(noteId: Int?) = viewModelScope.launch {
        if (noteId == null) {
            message.postValue(resourceProvider.getString(R.string.invalid_note_id_provided))
            return@launch
        }
        val response = repository.deleteNote(noteId)
        if (response == 0) {
            message.postValue(resourceProvider.getString(R.string.unable_to_delete_note))
        } else {
            message.postValue(resourceProvider.getString(R.string.note_deleted_success))
            fetchAllNotes()
        }
    }
}