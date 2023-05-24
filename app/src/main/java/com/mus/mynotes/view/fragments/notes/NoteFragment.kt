package com.mus.mynotes.view.fragments.notes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mus.mynotes.R
import com.mus.mynotes.constants.AppConstants
import com.mus.mynotes.databinding.FragmentNoteBinding
import com.mus.mynotes.models.notes.NotesModel
import com.mus.mynotes.utils.AppUtils
import com.mus.mynotes.utils.hide
import com.mus.mynotes.utils.show
import com.mus.mynotes.view.adapters.notes.NotesListingAdapter
import com.mus.mynotes.view.dialogs.DialogsUtils
import com.mus.mynotes.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var binding: FragmentNoteBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private var notesAdapter: NotesListingAdapter? = null
    private var itemClickedPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
            registerListeners()
            setUpAdapter()
            viewModel.fetchAllNotes()
        }
        return binding?.root
    }

    private fun registerObservers() {
        viewModel.message.observe(this) {
            AppUtils.showToast(it)
        }

        viewModel.noNotesFound.observe(this) {
            if (it) {
                binding?.tvTotalNotes?.text = getString(R.string.total_notes, 0)
                binding?.tvNoNotesFound?.show()
            } else {
                binding?.tvNoNotesFound?.hide()
            }
        }

        viewModel.notesList.observe(this) {
            notesAdapter?.addItems(it)
            updateTotalNotesSize(it.size)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            AppConstants.NOTES_BUNDLE_DATA,
            this
        ) { requestKey, result ->
            val isUpdateRequest = result.getBoolean(AppConstants.IS_UPDATE_REQUEST, false)
            val notesModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.getSerializable(AppConstants.NOTES_MODEL, NotesModel::class.java)
            } else {
                result.getSerializable(AppConstants.NOTES_MODEL) as NotesModel?
            }
            notesModel?.let { model ->
                if (isUpdateRequest) {
                    notesAdapter?.updateItem(itemClickedPosition, model)
                    itemClickedPosition = null
                } else {
                    notesAdapter?.addItem(model)
                }
            }
            // Update total notes size
            notesAdapter?.itemCount?.let { count ->
                updateTotalNotesSize(count)
            }
            binding?.tvNoNotesFound?.hide()
        }
    }

    private fun updateTotalNotesSize(count: Int) {
        binding?.tvTotalNotes?.text = getString(R.string.total_notes, count)

    }

    private fun setUpAdapter() {
        notesAdapter = NotesListingAdapter { isDelete, position, notesModel ->
            handleNotesClick(isDelete, position, notesModel)
        }
        binding?.rvNotes?.adapter = notesAdapter
    }

    private fun handleNotesClick(isDeleteClicked: Boolean, position: Int, notesModel: NotesModel?) {
        itemClickedPosition = position
        if (isDeleteClicked) {
            DialogsUtils.showConfirmationDialog(requireActivity()) { isDeleted ->
                if (isDeleted) {
                    viewModel.deleteNote(notesModel?.id)
                }
            }
        } else if (findNavController().currentDestination?.id == R.id.homeFragment) {
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.NOTES_MODEL, notesModel)
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment, bundle)

        }
    }

    private fun registerListeners() {
        binding?.ivAdd?.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.homeFragment) {
                findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
            }
        }
    }
}