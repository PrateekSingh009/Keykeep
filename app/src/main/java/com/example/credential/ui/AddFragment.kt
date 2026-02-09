package com.example.credential.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credential.R
import com.example.credential.adapter.IconPickerAdapter
import com.example.credential.data.CredentialViewModel
import com.example.credential.databinding.FragmentAddBinding
import com.example.credential.model.ItemCredential
import com.example.credential.utils.extensions.isNotEmptyOrShowError
import com.example.credential.utils.extensions.isValidPasswordOrShowError
import com.example.credential.utils.extensions.isValidUrlOrShowError
import com.example.credential.utils.extensions.replaceFragment
import com.example.credential.utils.utility.AppConstants
import com.example.credential.utils.utility.IconName
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
        setupToolbar()
        setupLiveValidation()
        if (isEditMode) {
            preloadData()
        }
        setupIconPicker()
        setupAddBtn()
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
            etUrl.isValidUrlOrShowError(required = false)
        }
//        binding.etEmail?.isValidEmailOrShowError(required = false)
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
            imageCardView.visibility = GONE
            tvTitle.text = if (isEditMode)
                AppConstants.EDIT_CREDENTIAL
            else
                AppConstants.ADD_CREDENTIAL
            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun preloadData() {
        existingCredential?.let { credential ->
            binding.apply {
                etTitle.setText(credential.title)
                etUsername.setText(credential.username)
                etPassword.setText(credential.password)
                etUrl.setText(credential.url)
                etNotes.setText(credential.notes)
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
                url = etUrl.text.toString().trim(),
                icon = selectedIconName,
                notes = etNotes.text.toString().trim(),
                email = null,
                phoneNumber = null,
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
                    minLength = 6,
                    requireComplexity = false
                )
            }
            etUrl.doAfterTextChanged {
                etUrl.isValidUrlOrShowError(required = false)
            }
//          etEmail?.doAfterTextChanged {
//              etEmail?.isValidEmailOrShowError(required = false)
//        }
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