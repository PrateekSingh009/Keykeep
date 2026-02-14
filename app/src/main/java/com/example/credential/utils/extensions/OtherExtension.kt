package com.example.credential.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.example.credential.database.entity.CredentialEntity
import com.example.credential.model.ItemCredential
import com.example.credential.utils.utility.EncryptionHelper
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.checkEmpty(s: String) {
    if (s.isEmpty()) {
        this.error = "Field is Required"
    }
    return
}

fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun View.toggleFieldVisibility(isVisible: Boolean, vararg linkedViews: View, onToggle: () -> Unit) {
    val state = if (isVisible) View.VISIBLE else View.GONE
    this.visibility = state
    linkedViews.forEach { it.visibility = state }
    onToggle()
}

fun List<CredentialEntity>.processList(encryptionHelper: EncryptionHelper): List<ItemCredential> {
    return this.toCredentialModelList().map {
        it.copy(
            password = try {
                encryptionHelper.decrypt(it.password)
            } catch (e: Exception) {
                ""
            }
        )
    }
}

fun View.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    post {
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
