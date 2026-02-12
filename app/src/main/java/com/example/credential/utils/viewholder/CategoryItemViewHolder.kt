package com.example.credential.utils.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.credential.R
import com.example.credential.databinding.ItemCategoryCardBinding
import com.example.credential.model.ItemCategory

class CategoryItemViewHolder(private val binding: ItemCategoryCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(category : ItemCategory, onCategoryClick : ((item : ItemCategory) -> Unit)) {
            binding.apply {
                tvCategoryName.text = category.name
                tvLoginCount.text = "${category.count} Logins"

                val resId = root.context.resources.getIdentifier(
                    category.icon, "drawable", root.context.packageName
                )
                ivCategoryIcon.setImageResource(if (resId != 0) resId else R.drawable.ic_default)

                root.setOnClickListener { onCategoryClick(category) }
            }
        }
}