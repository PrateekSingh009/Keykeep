package com.example.credential.utils.utility

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.credential.R

class MarginDividerItemDecoration(
    context: Context,
    private val leftMargin: Int,
    private val rightMargin: Int
) : RecyclerView.ItemDecoration() {

    private val divider: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.ic_line_divider)

    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val left = parent.paddingLeft + leftMargin
        val right = parent.width - parent.paddingRight - rightMargin

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}
