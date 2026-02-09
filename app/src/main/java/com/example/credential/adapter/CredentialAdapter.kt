package com.example.credential.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.credential.model.ItemCredential
import com.example.credential.databinding.ItemCredentialBinding
import com.example.credential.utils.viewholder.ListItemViewHolder

class CredentialAdapter(
    private val credentials: List<ItemCredential>,
    private val onClick: ((item: ItemCredential) -> Unit)?)
    : RecyclerView.Adapter<ListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            ItemCredentialBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(holder.itemView.context,credentials[position],onClick)
    }

    override fun getItemCount() = credentials.size
}