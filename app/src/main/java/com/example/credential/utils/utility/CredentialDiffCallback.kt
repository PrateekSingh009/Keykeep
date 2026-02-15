package com.example.credential.utils.utility

import androidx.recyclerview.widget.DiffUtil
import com.example.credential.model.ItemCredential

class CredentialDiffCallback : DiffUtil.ItemCallback<ItemCredential>() {
    override fun areItemsTheSame(oldItem: ItemCredential, newItem: ItemCredential): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemCredential, newItem: ItemCredential): Boolean {
        return oldItem == newItem
    }
}