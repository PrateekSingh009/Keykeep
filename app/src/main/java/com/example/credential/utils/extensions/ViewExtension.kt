package com.example.credential.utils.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


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

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.toggleFieldVisibility(isVisible: Boolean, vararg linkedViews: View, onToggle: () -> Unit) {
    val state = if (isVisible) View.VISIBLE else View.GONE
    this.visibility = state
    linkedViews.forEach { it.visibility = state }
    onToggle()
}