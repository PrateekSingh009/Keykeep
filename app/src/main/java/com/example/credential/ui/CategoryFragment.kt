package com.example.credential.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.credential.adapter.CategoryAdapter
import com.example.credential.data.CredentialViewModel
import com.example.credential.databinding.FragmentCategoryBinding
import com.example.credential.model.ItemCategory
import com.example.credential.utils.utility.AppConstants
import com.example.credential.utils.utility.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var _binding: FragmentCategoryBinding
    private val binding get() = _binding
    private val viewModel: CredentialViewModel by viewModels<CredentialViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupObserver()
        setupBtnClick()
        viewModel.getCategoryListFromDb()
    }

    private fun setupBtnClick() {
        binding.btnRemoveFilter.setOnClickListener {
            setupFilterFunctionality(null)
        }
    }

    private fun setupFilterFunctionality(item: ItemCategory?){
        parentFragmentManager.setFragmentResult(
            AppConstants.CATEGORY_FILTER,
            bundleOf(AppConstants.SELECTED_CATEGORY to item?.id)
        )
        parentFragmentManager.popBackStack()
    }

    private fun setupToolbar() {
        binding.includedLayout.apply {
            ivIcon.visibility = GONE
            tvTitle.text = TOOLBAR_TITLE
            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            ivEndIcon.visibility = GONE
        }
    }

    private fun setupObserver() {
        viewModel.categoryListLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    Log.e(ContentValues.TAG, ListFragment.LOADING)
                }

                is UIState.Failure -> {
                    Log.e(ContentValues.TAG, state.error.toString())
                }

                is UIState.Success -> {
                    state.data.let {
                        setupRecyclerView(it)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupRecyclerView(list: List<ItemCategory>?) {
        if (list != null) {
            binding.rvCategories.apply {
                adapter = CategoryAdapter(list, ::setupFilterFunctionality)
                layoutManager = GridLayoutManager(context, 2)
            }
        }
    }

    companion object {
        private const val TOOLBAR_TITLE = "Category"

        fun newInstance() = CategoryFragment()
    }

}