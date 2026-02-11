package com.example.credential.utils.extensions

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.checkEmpty(s : String) {
    if(s.isEmpty()) {
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
