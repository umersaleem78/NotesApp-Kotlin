package com.mus.mynotes.view.fragments.notes

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mus.mynotes.R
import com.mus.mynotes.constants.AppConstants
import com.mus.mynotes.databinding.FragmentAddNoteBinding
import com.mus.mynotes.models.notes.NotesModel
import com.mus.mynotes.utils.AppUtils
import com.mus.mynotes.viewModel.AddNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddNoteBinding
    private val viewModel: AddNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerObservers()
        getIntentData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerListeners()
        updatePageTitle()
    }

    private fun getIntentData() {
        val notesModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(AppConstants.NOTES_MODEL, NotesModel::class.java)
        } else {
            arguments?.getSerializable(AppConstants.NOTES_MODEL) as NotesModel?
        }
        // set data if it's a update request
        notesModel?.let { model ->
            viewModel.isUpdateRequest = true
            viewModel.title = model.title
            viewModel.description = model.description
            viewModel.notesModel = model
        }
    }

    private fun updatePageTitle() {
        if (viewModel.isUpdateRequest) {
            binding.tvTitle.text = getString(R.string.update_note)
        } else {
            binding.tvTitle.text = getString(R.string.create_note)
        }
    }

    private fun registerObservers() {
        viewModel.failureMessage.observe(this) {
            AppUtils.showToast(it)
        }

        viewModel.success.observe(this) {
            AppUtils.showToast(getString(R.string.data_entered_success))
            val bundle = Bundle()
            bundle.putBoolean(AppConstants.IS_UPDATE_REQUEST,viewModel.isUpdateRequest)
            bundle.putSerializable(AppConstants.NOTES_MODEL,it)
            requireActivity().supportFragmentManager.setFragmentResult(AppConstants.NOTES_BUNDLE_DATA,bundle)
            findNavController().popBackStack()
        }
    }

    private fun registerListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}