package com.example.credential.utils.viewholder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.credential.R
import com.example.credential.databinding.ItemIconPickerBinding

class IconPickerItemViewHolder(
    private val binding: ItemIconPickerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        context: Context,
        iconName: String,
        isSelected: Boolean,
        onIconClick: (String) -> Unit
    ) {
        val resId = context.resources.getIdentifier(
            iconName,
            context.getString(R.string.drawable),
            context.packageName
        )
        binding.apply {
            when (resId) {
                0 -> ivIcon.setImageResource(R.drawable.ic_default)
                else -> ivIcon.setImageResource(resId)
            }
        }

        binding.iconContainer.background = if (isSelected) {
            ContextCompat.getDrawable(context, R.drawable.bg_icon_selected)
        } else {
            null
        }

        binding.root.setOnClickListener {
            onIconClick(iconName)
        }
    }
}