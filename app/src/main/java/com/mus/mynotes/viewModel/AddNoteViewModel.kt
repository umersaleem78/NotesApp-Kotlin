package com.mus.mynotes.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mus.mynotes.R
import com.mus.mynotes.application.ResourceProvider
import com.mus.mynotes.models.notes.NotesModel
import com.mus.mynotes.repository.NotesRepository
import com.mus.mynotes.utils.AppUtils
import com.mus.mynotes.utils.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val resourcesProvider: ResourceProvider,
    private val notesRepository: NotesRepository
) : ViewModel() {

    var title: String? = null
    var description: String? = null
    var isUpdateRequest: Boolean = false
    var notesModel: NotesModel? = null

    var failureMessage: MutableLiveData<String> = MutableLiveData()
    var success: MutableLiveData<NotesModel> = MutableLiveData()

    fun addOrUpdateNote() = viewModelScope.launch {
        if (validateNoteRequest()) {
            if (isUpdateRequest) {
                val model = getUpdateNoteModel()
                notesRepository.updateNote(model)
                success.postValue(model)
            } else {
                val model = getAddNoteModel()
                val insertedId = notesRepository.addNote(model)
                model.id = insertedId.toInt()
                success.postValue(model)
            }
        }
    }

    private fun getAddNoteModel(): NotesModel {
        return NotesModel(
            title = title,
            description = description,
            timestamp = DateTimeUtils.getTimeStamp(),
            color = AppUtils.getRandomColor()
        )
    }

    private fun getUpdateNoteModel(): NotesModel {
        return NotesModel(
            id = notesModel?.id,
            title = title,
            description = description,
            timestamp = DateTimeUtils.getTimeStamp(),
            color = notesModel?.color
        )
    }

    private fun validateNoteRequest(): Boolean {
        if (title.isNullOrEmpty()) {
            failureMessage.postValue(resourcesProvider.getString(R.string.enter_title))
            return false
        } else if (description.isNullOrEmpty()) {
            failureMessage.postValue(resourcesProvider.getString(R.string.enter_description))
            return false
        }
        return true
    }

}