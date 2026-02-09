package com.example.credential.utils.viewholder

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.example.credential.R
import com.example.credential.databinding.ItemCredentialBinding
import com.example.credential.model.ItemCredential
import com.firebase.ui.auth.data.model.Resource
import com.google.common.io.Resources.getResource

class ListItemViewHolder(
    private val binding : ItemCredentialBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context,item : ItemCredential?,onClick : ((item : ItemCredential) -> Unit)?) {
        item?.let{
            binding.apply {
                ivLogo.setImageResource(context.resources.getIdentifier(it.icon,"drawable",context.packageName))
                tvTitle.text = it.title
                tvUsername.text = it.username
            }
            itemView.setOnClickListener {
                onClick?.invoke(item)
            }
        }
    }
}