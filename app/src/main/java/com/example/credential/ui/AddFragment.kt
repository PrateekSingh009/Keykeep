package com.example.credential.ui

import android.content.ContentValues
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credential.R
import com.example.credential.adapter.CategoryDropdownAdapter
import com.example.credential.adapter.IconPickerAdapter
import com.example.credential.data.CredentialViewModel
import com.example.credential.databinding.FragmentAddBinding
import com.example.credential.model.ItemCategory
import com.example.credential.model.ItemCredential
import com.example.credential.utils.extensions.hide
import com.example.credential.utils.extensions.isNotEmptyOrShowError
import com.example.credential.utils.extensions.isValidEmailOrShowError
import com.example.credential.utils.extensions.isValidNoteOrShowError
import com.example.credential.utils.extensions.isValidPasswordOrShowError
import com.example.credential.utils.extensions.isValidPhoneNumberOrShowError
import com.example.credential.utils.extensions.isValidUrlOrShowError
import com.example.credential.utils.extensions.replaceFragment
import com.example.credential.utils.extensions.show
import com.example.credential.utils.extensions.toggleFieldVisibility
import com.example.credential.utils.utility.AppConstants
import com.example.credential.utils.utility.IconName
import com.example.credential.utils.utility.UIState
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CredentialViewModel by viewModels<CredentialViewModel>()
    private var selectedIconName: String? = null
    private lateinit var allIconsList: List<String>
    private var isEditMode = false
    private var existingCredential: ItemCredential? = null
    private var selectedCategory: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            existingCredential = it.getParcelable(ARG_CREDENTIAL)
            isEditMode = existingCredential != null
        }
        if (isEditMode) {
            preloadData()
        }
        setupObserver()
        setupToolbar()
        setupLiveValidation()
        setupIconPicker()
        setupDetailAddAndRemoveBtn()
        setupAddBtn()
        viewModel.getCategoryListFromDb()
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
                        setupInitialCategorySelection(it[0])
                        setupCategoryDropdown(it)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupInitialCategorySelection(itemCategory: ItemCategory) {
        selectedCategory = itemCategory.id
        binding.apply{
            actvCategory.setText(itemCategory.name, false)
            tilCategory.setStartIconDrawable(
                requireContext().resources.getIdentifier(
                    itemCategory.icon, "drawable", requireContext().packageName
                )
            )
        }
    }

    private fun setupCategoryDropdown(categories : List<ItemCategory>) {

        val adapter = CategoryDropdownAdapter(requireContext(), categories)

        binding.actvCategory.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedCategory = categories[position].id
                binding.actvCategory.setText(categories[position].name, false)
                binding.tilCategory.setStartIconDrawable(
                    requireContext().resources.getIdentifier(
                        categories[position].icon, "drawable", requireContext().packageName
                    )
                )
            }
        }

        existingCredential?.categoryId?.let { catId ->
            val matching = categories.find { it.id == catId }
            if (matching != null) {
                binding.actvCategory.setText(matching.name, false)
                binding.tilCategory.setStartIconDrawable(
                    requireContext().resources.getIdentifier(
                        matching.icon, "drawable", requireContext().packageName
                    )
                )
                selectedCategory = matching.id
            }
        }
    }

    private fun setupDetailAddAndRemoveBtn() {
        binding.apply {
            setupFieldLogic(btnAddUrl, btnRemoveUrl, layUrl, tilUrl)
            setupFieldLogic(btnAddEmail, btnRemoveEmail, layEmail, tilEmail)
            setupFieldLogic(btnAddPhone, btnRemovePhone, layPhoneNumber, tilPhoneNumber)
            setupFieldLogic(btnAddNote, btnRemoveNote, layNote, tilNote)
        }
    }

    private fun showBlock(labelLay: View, inputTil: View, addBtn: View) {
        labelLay.show()
        inputTil.show()
        addBtn.hide()
    }

    private fun setupFieldLogic(addBtn: View, removeBtn: View, labelLay: View, inputTil: View) {
        addBtn.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.textContentLayout)
            labelLay.toggleFieldVisibility(true, inputTil) {
                addBtn.hide()
                checkAllAddBtnGone()
            }
        }

        removeBtn.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.textContentLayout)
            labelLay.toggleFieldVisibility(false, inputTil) {
                addBtn.show()
                (inputTil.findViewById<TextInputLayout>(inputTil.id).editText)?.text?.clear()
                checkAllAddBtnGone()
            }
        }
    }

    private fun checkAllAddBtnGone() {
        binding.apply {
            val anyVisible = listOf(btnAddUrl, btnAddPhone, btnAddNote, btnAddEmail)
                .any { it.visibility == VISIBLE }

            tvAddDetail.apply { if (anyVisible) show() else hide() }
        }
    }

    private fun setupAddBtn() {
        binding.btnSave.apply {
            text = if (isEditMode) AppConstants.UPDATE else AppConstants.SAVE
            setOnClickListener {
                if (validateAllFields()) {
                    saveOrUpdateCredential()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateAllFields(): Boolean {
        var isValid = true
        binding.apply {
            if (!etTitle.isNotEmptyOrShowError(getString(R.string.empty_title_error_message))) isValid =
                false
            if (!etUsername.isNotEmptyOrShowError(getString(R.string.empty_username_error_message))) isValid =
                false
            if (!etPassword.isValidPasswordOrShowError(
                    minLength = 6,
                    requireComplexity = false,
                    errorMsg = getString(R.string.password_error_message)
                )
            ) isValid = false
            if (layUrl.isVisible) {
                if (!etUrl.isValidUrlOrShowError(required = true)) isValid = false
            }

            if (layEmail.isVisible) {
                if (!etEmail.isValidEmailOrShowError(required = true)) isValid = false
            }

            if (layPhoneNumber.isVisible) {
                if (!etPhoneNumber.isValidPhoneNumberOrShowError(required = true)) isValid = false
            }

            if (layNote.isVisible) {
                if (!etNote.isValidNoteOrShowError()) isValid = false
            }
        }
        return isValid
    }

    private fun onItemSelection(iconName: String) {
        selectedIconName = iconName
        val resId = resources.getIdentifier(
            iconName,
            getString(R.string.drawable),
            requireContext().packageName
        )
        if (resId != 0) {
            binding.ivSelectedIcon.setImageResource(resId)
        } else {
            binding.ivSelectedIcon.setImageResource(R.drawable.ic_default)
        }
    }

    private fun setupToolbar() {
        binding.includedLayout.apply {
            imageCardView.hide()
            tvTitle.text = if (isEditMode)
                AppConstants.EDIT_CREDENTIAL
            else
                AppConstants.ADD_CREDENTIAL
            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            ivEndIcon.hide()
        }
    }

    private fun preloadData() {
        existingCredential?.let { credential ->
            binding.apply {
                etTitle.setText(credential.title)
                etUsername.setText(credential.username)
                etPassword.setText(credential.password)
                if (!credential.url.isNullOrBlank()) {
                    showBlock(layUrl, tilUrl, btnAddUrl)
                    etUrl.setText(credential.url)
                }

                if (!credential.email.isNullOrBlank()) {
                    showBlock(layEmail, tilEmail, btnAddEmail)
                    etEmail.setText(credential.email)
                }
                if (!credential.phoneNumber.isNullOrBlank()) {
                    showBlock(layPhoneNumber, tilPhoneNumber, btnAddPhone)
                    etPhoneNumber.setText(credential.phoneNumber)
                }
                if (!credential.notes.isNullOrBlank()) {
                    showBlock(layNote, tilNote, btnAddNote)
                    etNote.setText(credential.notes)
                }
                checkAllAddBtnGone()
            }
        }
    }

    private fun setupIconPicker() {
        allIconsList = IconName.getAllIcons()

        if (isEditMode) updateSelectedIconInPicker(existingCredential?.icon)
        else updateSelectedIconInPicker(allIconsList[0])

        val adapter = IconPickerAdapter(
            allIconsList,
            ::onItemSelection,
            selectedIconName?.let { allIconsList.indexOf(it) } ?: 0)

        binding.apply {
            rvIconPicker.adapter = adapter
            rvIconPicker.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
        }
    }

    private fun updateSelectedIconInPicker(icon: String?) {
        selectedIconName = icon
        val resId = resources.getIdentifier(
            icon,
            getString(R.string.drawable),
            requireContext().packageName
        )
        binding.ivSelectedIcon.setImageResource(if (resId != 0) resId else R.drawable.ic_default)
    }

    private fun saveOrUpdateCredential() {
        binding.apply {
            val credential = ItemCredential(
                id = existingCredential?.id ?: 0,
                title = etTitle.text.toString().trim(),
                username = etUsername.text.toString().trim(),
                password = etPassword.text.toString(),
                icon = selectedIconName,
                url = if (layUrl.isVisible) etUrl.text.toString().trim() else "",
                email = if (layEmail.isVisible) etEmail.text.toString().trim() else "",
                phoneNumber = if (layPhoneNumber.isVisible) etPhoneNumber.text.toString()
                    .trim() else "",
                notes = if (layNote.isVisible) etNote.text.toString().trim() else "",
                categoryId = selectedCategory
            )
            viewModel.upsertCredential(credential)
            if (isEditMode) {
                Toast.makeText(requireContext(), CREDENTIAL_UPDATED, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), CREDENTIAL_SAVED, Toast.LENGTH_SHORT).show()
            }
            parentFragmentManager.replaceFragment(ListFragment(), R.id.fragment_container)
        }
    }

    private fun setupLiveValidation() {
        binding.apply {
            etTitle.doAfterTextChanged {
                etTitle.isNotEmptyOrShowError(getString(R.string.title_is_required))
            }
            etUsername.doAfterTextChanged {
                etUsername.isNotEmptyOrShowError(getString(R.string.username_is_required))
            }
            etPassword.doAfterTextChanged {
                etPassword.isValidPasswordOrShowError(
                    minLength = 4,
                    requireComplexity = false
                )
            }
            etUrl.doAfterTextChanged {
                if (layUrl.isVisible) etUrl.isValidUrlOrShowError(required = true)
            }
            etEmail.doAfterTextChanged {
                if (layEmail.isVisible) etEmail.isValidEmailOrShowError(required = true)
            }
            etPhoneNumber.doAfterTextChanged {
                if (layPhoneNumber.isVisible) etPhoneNumber.isValidPhoneNumberOrShowError(required = true)
            }
            etNote.doAfterTextChanged {
                if (layNote.isVisible) etNote.isValidNoteOrShowError()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_CREDENTIAL = "arg_credential"
        const val CREDENTIAL_SAVED = "Credential Saved"
        const val CREDENTIAL_UPDATED = "Credential Updated"

        fun newInstance(credential: ItemCredential?): AddFragment {
            val fragment = AddFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CREDENTIAL, credential)
            fragment.arguments = bundle
            return fragment
        }
    }
}