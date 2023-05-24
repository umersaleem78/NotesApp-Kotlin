package com.mus.mynotes.view.fragments.todo

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
import com.mus.mynotes.databinding.FragmentAddTodoBinding
import com.mus.mynotes.models.todo.TodoItemMainModel
import com.mus.mynotes.utils.AppUtils
import com.mus.mynotes.view.adapters.todo.AddTodoItemAdapter
import com.mus.mynotes.viewModel.TodoItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTodoFragment : Fragment() {

    private var binding: FragmentAddTodoBinding? = null
    private val viewModel: TodoItemViewModel by viewModels()
    private var adapter: AddTodoItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerObservers()
        getIntentData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentAddTodoBinding.inflate(layoutInflater, container, false)
            binding?.viewModel = viewModel
            binding?.lifecycleOwner = this
            binding?.tvTitle?.text = getString(R.string.create_todo_list)
            registerListeners()
            setUpAdapter()
            setUpIntentData()
        }
        return binding?.root
    }

    private fun registerObservers() {
        viewModel.success.observe(this) {
            AppUtils.showToast(getString(R.string.data_entered_success))
            val bundle = Bundle()
            bundle.putBoolean(AppConstants.IS_UPDATE_REQUEST, viewModel.isUpdateRequest)
            bundle.putSerializable(AppConstants.ADD_TODO_MODEL, it)
            requireActivity().supportFragmentManager.setFragmentResult(
                AppConstants.ADD_TODO_BUNDLE_DATA,
                bundle
            )
            findNavController().popBackStack()
        }
    }

    private fun getIntentData() {
        viewModel.model = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(AppConstants.ADD_TODO_MODEL, TodoItemMainModel::class.java)
        } else {
            arguments?.getSerializable(AppConstants.ADD_TODO_MODEL) as TodoItemMainModel?
        }
        if (viewModel.model != null) {
            viewModel.isUpdateRequest = true
        }
    }

    private fun registerListeners() {
        binding?.ivBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpAdapter() {
        adapter = AddTodoItemAdapter {
            handleListener()
        }
        binding?.rvTodoItems?.adapter = adapter
        // add new item only if it's not a update request
        if (!viewModel.isUpdateRequest) {
            adapter?.addItems(viewModel.fetchTodoMainItemModel().item)
        }
    }

    private fun handleListener() {
        adapter?.fetchItemsList()?.let { items ->
            viewModel.todoItemsList.clear()
            viewModel.todoItemsList.addAll(items)
        }
    }

    private fun setUpIntentData() {
        // set data if it's a update request
        viewModel.model?.let { info ->
            viewModel.title = info.title
            viewModel.description = info.description
            viewModel.model = info
            viewModel.fetchTodoMainItemModel().item?.let { info.item?.addAll(it) }
            adapter?.addItems(info.item)
            binding?.tvTitle?.text = getString(R.string.update_todo_list)
        }
    }
}