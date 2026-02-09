package com.example.credential.utils.extensions

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun TextInputEditText.showError(message: String?) {
    (parent?.parent as? TextInputLayout)?.let { til ->
        til.error = message
        til.isErrorEnabled = !message.isNullOrBlank()
    } ?: run {
        this.error = message
    }
}

fun TextInputEditText.clearError() {
    showError(null)
}

fun TextInputEditText.isNotEmptyOrShowError(errorMsg: String = "This field is required"): Boolean {
    val value = text?.toString()?.trim() ?: ""
    return if (value.isBlank()) {
        showError(errorMsg)
        false
    } else {
        clearError()
        true
    }
}

fun TextInputEditText.isValidEmailOrShowError(
    required: Boolean = true,
    errorMsg: String = "Please enter a valid email"
): Boolean {
    val value = text?.toString()?.trim() ?: ""

    if (value.isBlank()) {
        return if (required) {
            showError("Email is required")
            false
        } else {
            clearError()
            true
        }
    }

    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    return if (emailRegex.matches(value)) {
        clearError()
        true
    } else {
        showError(errorMsg)
        false
    }
}

fun TextInputEditText.isValidUrlOrShowError(
    required: Boolean = false,
    errorMsg: String = "Please enter a valid URL"
): Boolean {
    val value = text?.toString()?.trim() ?: ""

    if (value.isBlank()) {
        return if (required) {
            showError("URL is required")
            false
        } else {
            clearError()
            true
        }
    }

    val urlRegex = Regex("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$")
    return if (urlRegex.matches(value)) {
        clearError()
        true
    } else {
        showError(errorMsg)
        false
    }
}

fun TextInputEditText.isValidPasswordOrShowError(
    minLength: Int = 8,
    requireComplexity: Boolean = false,
    errorMsg: String = "Password must be at least $minLength characters"
): Boolean {
    val value = text?.toString() ?: ""

    if (value.isBlank()) {
        showError("Password is required")
        return false
    }

    if (value.length < minLength) {
        showError(errorMsg)
        return false
    }

    if (requireComplexity) {
        val hasUpper = value.any { it.isUpperCase() }
        val hasLower = value.any { it.isLowerCase() }
        val hasDigit = value.any { it.isDigit() }
        val hasSpecial = value.any { "!@#\$%^&*()_+-=[]{}|;:'\",.<>?/`~".contains(it) }

        if (! (hasUpper && hasLower && hasDigit && hasSpecial) ) {
            showError("Password must contain uppercase, lowercase, number & special character")
            return false
        }
    }

    clearError()
    return true
}
