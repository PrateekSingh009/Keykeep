package com.example.credential.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.credential.R
import com.example.credential.databinding.ItemDropdownCategoryBinding
import com.example.credential.model.ItemCategory

class CategoryDropdownAdapter(
    context: Context,
    private val categories: List<ItemCategory>
) : ArrayAdapter<ItemCategory>(context, 0, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createOrReuseView(convertView, parent, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createOrReuseView(convertView, parent, position)
    }

    private fun createOrReuseView(
        convertView: View?,
        parent: ViewGroup,
        position: Int
    ): View {
        val binding = if (convertView == null) {
            ItemDropdownCategoryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        } else {
            ItemDropdownCategoryBinding.bind(convertView)
        }

        val item = getItem(position) ?: return binding.root

        binding.apply {
            val resId = context.resources.getIdentifier(
                item.icon,
                "drawable",
                context.packageName
            )
            if (resId != 0) {
                ivCategoryIcon.setImageResource(resId)
            } else {
                ivCategoryIcon.setImageResource(R.drawable.ic_default)
            }

            tvCategoryName.text = item.name
        }
        return binding.root
    }
}