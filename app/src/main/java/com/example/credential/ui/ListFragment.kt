package com.example.credential.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credential.R
import com.example.credential.adapter.CredentialAdapter
import com.example.credential.data.CredentialViewModel
import com.example.credential.databinding.FragmentListBinding
import com.example.credential.model.ItemCredential
import com.example.credential.utils.utility.MarginDividerItemDecoration
import com.example.credential.utils.utility.UIState
import com.example.credential.utils.extensions.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var _binding: FragmentListBinding
    private val binding get() = _binding
    private val viewModel: CredentialViewModel by viewModels<CredentialViewModel>()
    private var isFilterApplied = false // Keep this in viewmodel to save it when the fragment is destroyed

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBar()
        setupObserver()
        setupListeners()
        setupFabClick()
        viewModel.getCredentialListFromDb()
    }

    private fun setupToolBar() {
        binding.toolbar.apply {
            title = getString(R.string.app_name)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_search -> {
                        handleSearchClick()
                        true
                    }
                    R.id.action_filter -> {
                        toggleFilterState(menuItem)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun handleSearchClick() {
        TODO("Not yet implemented")
    }

    private fun setupFabClick(){
        binding.btnAdd.setOnClickListener {
            parentFragmentManager.replaceFragment(AddFragment.newInstance(null), R.id.fragment_container,true)
        }
    }

    private fun onItemClick(item: ItemCredential) {
        parentFragmentManager.replaceFragment(DetailFragment.newInstance(item), R.id.fragment_container,true)
    }

    private fun setupListeners(){
        setFragmentResultListener("category_filter") { _, bundle ->
            val category = bundle.getString("selected_category")
//            viewModel.filterByCategory(category)
        }

    }

    private fun toggleFilterState(filterItem: MenuItem) {
        // 1. Flip the state
        isFilterApplied = !isFilterApplied

        // 2. Change the icon based on state
        if (isFilterApplied) {
            filterItem.setIcon(R.drawable.ic_filter_off) // Your "Applied" icon
            parentFragmentManager.replaceFragment(CategoryFragment.newInstance(), R.id.fragment_container,true)
//            Toast.makeText(requireContext(), "Filter Applied", Toast.LENGTH_SHORT).show()
        } else {
            filterItem.setIcon(R.drawable.ic_filter) // Your "Default" icon
            Toast.makeText(requireContext(), "Filter Removed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObserver() {
        viewModel.credentialListLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    Log.e(ContentValues.TAG, LOADING)
                }

                is UIState.Failure -> {
                    Log.e(ContentValues.TAG, state.error.toString())
                }

                is UIState.Success -> {
                    state.data.let {
                        Log.i(CREDENTIAL_ITEM_LIST, it.toString())
                        setupRecyclerView(it)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupRecyclerView(list: List<ItemCredential>?) {
        val divider = MarginDividerItemDecoration(
            requireContext(),
            leftMargin = resources.getDimensionPixelSize(R.dimen.divider_margin_start),
            rightMargin = resources.getDimensionPixelSize(R.dimen.divider_margin_end)
        )
        if (list != null) {
            binding.rvCredentials.apply {
                this.addItemDecoration(divider)
                adapter = CredentialAdapter(list, ::onItemClick)
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    companion object {
        const val LOADING = "Loading"
        const val CREDENTIAL_ITEM_LIST = "Credential Item List"
        fun newInstance() = ListFragment()
    }
}