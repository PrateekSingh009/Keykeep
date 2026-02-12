package com.example.credential.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.example.credential.adapter.CategoryAdapter
import com.example.credential.databinding.FragmentCategoryBinding
import com.example.credential.model.ItemCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var _binding: FragmentCategoryBinding
    private val binding get() = _binding

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
        setupRecyclerView(getCategoryList())
    }

    private fun setupToolbar() {
        binding.includedLayout.apply {
            ivIcon.visibility = GONE
            tvTitle.text = TOOLBAR_TITLE
            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun setupRecyclerView(list: List<ItemCategory>?) {
        if (list != null) {
            binding.rvCategories.apply {
                adapter = CategoryAdapter(list, ::onItemClick)
                layoutManager = GridLayoutManager(context, 2)
            }
        }
    }

    private fun getCategoryList(): List<ItemCategory> {
        return listOf(
            ItemCategory("Social", "ic_social", 2),
            ItemCategory("Shopping", "ic_shopping", 5),
            ItemCategory("Work","ic_work",4),
            ItemCategory("Bank","ic_bank",8)
        )
    }

    private fun onItemClick(item: ItemCategory) {
        parentFragmentManager.setFragmentResult(
            "category_filter",
            bundleOf("selected_category" to item.name)
        )
        parentFragmentManager.popBackStack()
    }

    companion object {
        private const val TOOLBAR_TITLE = "Category"

        fun newInstance() = CategoryFragment()
    }

}