package com.example.credential.ui

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import com.example.credential.R
import com.example.credential.data.CredentialViewModel
import com.example.credential.databinding.FragmentDetailBinding
import com.example.credential.model.ItemCredential
import com.example.credential.utils.extensions.hide
import com.example.credential.utils.extensions.replaceFragment
import com.example.credential.utils.extensions.show
import com.example.credential.utils.utility.AppConstants
import com.example.credential.utils.utility.StringHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var _binding: FragmentDetailBinding
    private val binding get() = _binding
    private val viewModel: CredentialViewModel by viewModels<CredentialViewModel>()
    private lateinit var itemCredential: ItemCredential

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            itemCredential = it.getParcelable(AddFragment.ARG_CREDENTIAL)!!
        }
        setupToolbar()
        setupViews()
        setupButtons()
    }

    private fun setupButtons() {
        setupEditAndDelete()
        setupCopyButton()
        setupBtnPasswordVisibility()
    }

    private fun setupEditAndDelete() {
        binding.apply {
            btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(itemCredential) {
                    parentFragmentManager.replaceFragment(
                        ListFragment.newInstance(),
                        R.id.fragment_container,
                        true
                    )
                }
            }
            btnEdit.setOnClickListener {
                parentFragmentManager.replaceFragment(
                    AddFragment.newInstance(itemCredential),
                    R.id.fragment_container,
                    true
                )
            }
        }
    }

    private fun showDeleteConfirmationDialog(credential: ItemCredential, onConfirmed: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(DELETE_CREDENTIAL)
            .setMessage(getString(R.string.delete_confirmation_message, credential.title))
            .setIcon(R.drawable.ic_delete_bin)
            .setNegativeButton(AppConstants.CANCEL) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(AppConstants.DELETE) { dialog, _ ->
                viewModel.deleteCredential(credential.id)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.credential_delete),
                    Toast.LENGTH_SHORT
                ).show()
                onConfirmed()
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
            .apply {
                getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.red)
                )
            }
    }

    private fun setupToolbar() {
        binding.includedLayout.apply {
            ivIcon.setImageResource(
                requireContext().resources.getIdentifier(
                    itemCredential.icon,
                    getString(R.string.drawable),
                    requireContext().packageName
                )
            )
            tvTitle.text = itemCredential.title
            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            ivEndIcon.hide()
        }
    }

    private fun setupBtnPasswordVisibility() {
        binding.apply {
            btnTogglePassword.setOnClickListener {
                if (tvPassword.text == "*".repeat(itemCredential.password.length)) {
                    tvPassword.text = itemCredential.password
                    btnTogglePassword.setImageResource(
                        requireContext().resources.getIdentifier(
                            StringHelper.passwordInvisible,
                            getString(R.string.drawable),
                            requireContext().packageName
                        )
                    )
                } else {
                    tvPassword.text = "*".repeat(itemCredential.password.length)
                    btnTogglePassword.setImageResource(
                        requireContext().resources.getIdentifier(
                            StringHelper.passwordVisible,
                            getString(R.string.drawable),
                            requireContext().packageName
                        )
                    )
                }
            }
        }
    }

    private fun setupViews() {
        binding.apply {
            itemCredential.let {
                tvUsername.text = it.username
                tvPassword.text = "*".repeat(it.password.length)
                bindOptionalRow(it.url, rowWebsite, sepWebsite, tvWebsite)
                bindOptionalRow(it.email, rowEmail, sepEmail, tvEmail)
                bindOptionalRow(it.phoneNumber, rowPhoneNumber, sepPhoneNumber, tvPhoneNumber)

                if (it.notes.isNullOrBlank()) {
                    rowNote.hide()
                } else {
                    rowNote.show()
                    tvNotes.text = it.notes
                }
            }
        }
    }

    private fun bindOptionalRow(data: String?, row: View, separator: View, textView: TextView) {
        if (data.isNullOrBlank()) {
            row.hide()
            separator.hide()
        } else {
            row.show()
            separator.show()
            textView.text = data
        }
    }

    private fun setupCopyButton() {
        binding.apply {
            btnCopyUsername.setOnClickListener {
                copyToClipboard(itemCredential.username,USERNAME_COPIED)
            }
            btnCopyWebsite.setOnClickListener {
                copyToClipboard(itemCredential.url,URL_COPIED)
            }
            btnCopyPassword.setOnClickListener {
                copyToClipboard(itemCredential.password,PASSWORD_COPIED)
            }
            btnCopyPhoneNumber.setOnClickListener {
                copyToClipboard(itemCredential.phoneNumber,PHONE_NUMBER_COPIED)
            }
            btnCopyEmail.setOnClickListener {
                copyToClipboard(itemCredential.email,EMAIL_COPIED)
            }
        }
    }

    private fun copyToClipboard(textToCopy: String?,label: String){
        if (textToCopy.isNullOrBlank()) return

        val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText(label, textToCopy)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(
            requireContext(),
            getString(R.string.copied_to_clipboard),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val ARG_CREDENTIAL = "arg_credential"
        const val PASSWORD_COPIED = "Password Copied"
        const val URL_COPIED = "URL Copied"
        const val PHONE_NUMBER_COPIED = "Phone Number Copied"
        const val EMAIL_COPIED = "Email Copied"
        const val USERNAME_COPIED = "Username Copied"
        const val DELETE_CREDENTIAL = "Delete Credential"

        fun newInstance(credential: ItemCredential?): DetailFragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_CREDENTIAL, credential)
            fragment.arguments = bundle
            return fragment
        }
    }
}