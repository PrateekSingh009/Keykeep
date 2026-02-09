package com.example.credential.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.credential.databinding.ItemIconPickerBinding
import com.example.credential.utils.viewholder.IconPickerItemViewHolder

class IconPickerAdapter(
    private val icons: List<String>,
    private val onIconSelected: (String) -> Unit,
    private val selectedIconPosition: Int
) : RecyclerView.Adapter<IconPickerItemViewHolder>() {

    private var selectedPosition = selectedIconPosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconPickerItemViewHolder {
        return IconPickerItemViewHolder(
            ItemIconPickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = icons.size

    override fun onBindViewHolder(holder: IconPickerItemViewHolder, position: Int) {
        val iconName = icons[position]
        val isSelected = position == selectedPosition

        holder.bind(
            context = holder.itemView.context,
            iconName = iconName,
            isSelected = isSelected,
            onIconClick = { clickedIconName ->
                val previousPosition = selectedPosition
                selectedPosition = position
                if (previousPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPosition)
                }
                notifyItemChanged(position)
                onIconSelected(clickedIconName)
            }
        )
    }
}