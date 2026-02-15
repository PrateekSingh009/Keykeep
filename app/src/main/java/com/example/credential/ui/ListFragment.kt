package com.example.credential.ui

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credential.R
import com.example.credential.adapter.CredentialAdapter
import com.example.credential.data.CredentialViewModel
import com.example.credential.databinding.FragmentListBinding
import com.example.credential.model.ItemCredential
import com.example.credential.utils.extensions.hideKeyboard
import com.example.credential.utils.utility.MarginDividerItemDecoration
import com.example.credential.utils.utility.UIState
import com.example.credential.utils.extensions.replaceFragment
import com.example.credential.utils.extensions.showKeyboard
import com.example.credential.utils.utility.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var _binding: FragmentListBinding
    private val binding get() = _binding
    private val viewModel: CredentialViewModel by viewModels<CredentialViewModel>()
    private lateinit var adapter: CredentialAdapter

    private var searchJob: Job? = null
    private var currentSearchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CredentialAdapter { credential ->
            onItemClick(credential)
        }

        setupToolBar()
        setupRecyclerView()
        setupObserver()
        setupListeners()
        setupFabClick()
        binding.toolbar.menu.findItem(R.id.action_filter).let {
            updateFilterIcon(it)
        }
        if (viewModel.credentialListLiveData.value == null) {
            viewModel.getCredentialListFromDb(null)
        }
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
                        handleFilterClick()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun handleSearchClick() {
        toggleSearch(true)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                if (query != currentSearchQuery) {
                    currentSearchQuery = query
                    debounceSearch(query)
                }
            }
        })
    }

    private fun debounceSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(300)
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewModel.getCredentialListFromDb(viewModel.currentFilterId)
        } else {
            viewModel.filterByQuery(query)
        }
    }

    private fun toggleSearch(isSearching: Boolean) {
        val transition = TransitionSet().apply {
            addTransition(Fade())
            addTransition(ChangeBounds())
            duration = 250
        }

        TransitionManager.beginDelayedTransition(binding.toolbarContainer, transition)

        binding.apply {
            if (isSearching) {
                searchBarLayout.visibility = View.VISIBLE
                toolbar.title = ""
                etSearch.requestFocus()
                etSearch.showKeyboard()
            } else {
                searchBarLayout.visibility = View.GONE
                toolbar.title = getString(R.string.app_name)
                etSearch.text.clear()
                etSearch.hideKeyboard()
            }
        }
    }

    private fun setupFabClick() {
        binding.btnAdd.setOnClickListener {
            parentFragmentManager.replaceFragment(
                AddFragment.newInstance(null),
                R.id.fragment_container,
                true
            )
        }
    }

    private fun onItemClick(item: ItemCredential) {
        parentFragmentManager.replaceFragment(
            DetailFragment.newInstance(item),
            R.id.fragment_container,
            true
        )
    }

    private fun setupListeners() {
        binding.ivSearchIconInner.setOnClickListener {
            toggleSearch(false)
        }

        setFragmentResultListener(AppConstants.CATEGORY_FILTER) { _, bundle ->
            val categoryId = bundle.getInt(AppConstants.SELECTED_CATEGORY)
            viewModel.currentFilterId = if (categoryId == 0) null else categoryId
            viewModel.getCredentialListFromDb(categoryId)
            binding.toolbar.menu.findItem(R.id.action_filter).let {
                updateFilterIcon(it)
            }
        }
    }

    private fun updateFilterIcon(filterItem: MenuItem?) {
        filterItem?.let {
            if (viewModel.currentFilterId != null && viewModel.currentFilterId != 0) {
                it.setIcon(R.drawable.ic_filter_off)
            } else {
                it.setIcon(R.drawable.ic_filter)
            }
        }
    }

    private fun handleFilterClick() {
        parentFragmentManager.replaceFragment(
            CategoryFragment.newInstance(),
            R.id.fragment_container,
            true
        )
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
                        adapter.submitList(it)
                        setupEmptyListView(it)
                    }
                }

                else -> {}
            }
        }
    }

    private fun setupRecyclerView() {
        val divider = MarginDividerItemDecoration(
            requireContext(),
            leftMargin = resources.getDimensionPixelSize(R.dimen.divider_margin_start),
            rightMargin = resources.getDimensionPixelSize(R.dimen.divider_margin_end)
        )
        binding.rvCredentials.apply {
            this.addItemDecoration(divider)
            adapter = this@ListFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun setupEmptyListView(list: List<ItemCredential>?) {
        val isEmpty = list.isNullOrEmpty()
        val rootLayout = binding.root.parent as? ViewGroup ?: return
        if (isEmpty) {
            TransitionManager.beginDelayedTransition(rootLayout)
            binding.rvCredentials.visibility = GONE
            binding.ivEmpty.visibility = VISIBLE
        } else {
            binding.rvCredentials.visibility = VISIBLE
            binding.ivEmpty.visibility = GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
    }


    companion object {
        const val LOADING = "Loading"
        const val CREDENTIAL_ITEM_LIST = "Credential Item List"
        fun newInstance() = ListFragment()
    }
}