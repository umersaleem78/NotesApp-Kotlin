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
import com.mus.mynotes.databinding.FragmentTodoBinding
import com.mus.mynotes.models.todo.TodoItemMainModel
import com.mus.mynotes.utils.AppUtils
import com.mus.mynotes.utils.hide
import com.mus.mynotes.utils.show
import com.mus.mynotes.view.adapters.todo.TodoParentItemListingAdapter
import com.mus.mynotes.view.dialogs.DialogsUtils
import com.mus.mynotes.viewModel.TodoItemViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoFragment : Fragment() {

    private var binding: FragmentTodoBinding? = null
    private val viewModel: TodoItemViewModel by viewModels()
    private var todoItemAdapter: TodoParentItemListingAdapter? = null
    private var itemClickedPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("MainClass => on Create")
        registerObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentTodoBinding.inflate(layoutInflater, container, false)
            registerListeners()
            setUpAdapter()
            println("MainClass => on Create view")
            viewModel.fetchTodoItemsList()
       }
        return binding?.root
    }

    private fun registerObservers() {
        println("MainClass => register Observer")
        viewModel.message.observe(this) {
            AppUtils.showToast(it)
        }

        viewModel.loader.observe(this) {
            if (it) {
                binding?.progressBar?.show()
            } else {
                binding?.progressBar?.hide()
            }
        }

        viewModel.todoItemsListResponse.observe(this) {
            println("MainClass => todo items response observer")
            if (it.isEmpty()) {
                binding?.tvNoItemsFound?.show()
            } else {
                binding?.tvNoItemsFound?.hide()
            }
            updateTotalItemsCount(it.size)
            todoItemAdapter?.addItems(it)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            AppConstants.ADD_TODO_BUNDLE_DATA,
            this
        ) { requestKey, result ->
            val isUpdateRequest = result.getBoolean(AppConstants.IS_UPDATE_REQUEST, false)
            val model = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.getSerializable(AppConstants.ADD_TODO_MODEL, TodoItemMainModel::class.java)
            } else {
                result.getSerializable(AppConstants.ADD_TODO_MODEL) as TodoItemMainModel?
            }
            model?.let { info ->
                if (isUpdateRequest) {
                    todoItemAdapter?.updateItem(itemClickedPosition, info)
                    itemClickedPosition = null
                } else {
                    todoItemAdapter?.addItem(info)
                }
            }
            // Update total notes size & 'No notes found' status
            val itemCount = todoItemAdapter?.itemCount ?: 0
            updateTotalItemsCount(itemCount)
            if (itemCount > 0) {
                binding?.tvNoItemsFound?.hide()
            } else {
                binding?.tvNoItemsFound?.show()
            }
        }
    }

    private fun updateTotalItemsCount(count: Int) {
        binding?.tvTotalItems?.text = getString(R.string.total_notes, count)
    }

    private fun registerListeners() {
        binding?.ivAdd?.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.todoFragment) {
                findNavController().navigate(R.id.action_todoFragment_to_addTodoFragment)
            }
        }
    }

    private fun setUpAdapter() {
        todoItemAdapter = TodoParentItemListingAdapter { isDeleted, position, item ->
            handleItemClick(isDeleted, position, item)
        }
        binding?.rvTodoItems?.adapter = todoItemAdapter
    }

    private fun handleItemClick(
        isDeleteClicked: Boolean,
        position: Int,
        model: TodoItemMainModel?
    ) {
        itemClickedPosition = position
        if (isDeleteClicked) {
            DialogsUtils.showConfirmationDialog(requireActivity()) { isDeleted ->
                if (isDeleted) {
                    viewModel.deleteTodoItem(model?.id)
                }
            }
        } else if (findNavController().currentDestination?.id == R.id.todoFragment) {
            val bundle = Bundle()
            bundle.putSerializable(AppConstants.ADD_TODO_MODEL, model)
            findNavController().navigate(R.id.action_todoFragment_to_addTodoFragment, bundle)
        }

    }

}