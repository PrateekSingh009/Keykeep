package com.example.credential.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.credential.databinding.ItemCategoryCardBinding
import com.example.credential.model.ItemCategory
import com.example.credential.utils.viewholder.CategoryItemViewHolder

class CategoryAdapter(
    private val categories: List<ItemCategory>,
    private val onCategoryClick: (ItemCategory) -> Unit
) : RecyclerView.Adapter<CategoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            ItemCategoryCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.bind(categories[position],onCategoryClick)
    }

    override fun getItemCount() = categories.size
}