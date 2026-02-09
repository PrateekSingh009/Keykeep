package com.example.credential.utils.extensions

import com.example.credential.database.entity.CredentialEntity
import com.example.credential.model.ItemCredential

fun CredentialEntity.toModel(): ItemCredential {
    return ItemCredential(
        id = id,
        title = title,
        username = username,
        password = password,
        url = url,
        icon = icon,
        notes = notes,
        email = email,
        phoneNumber = phoneNumber
    )
}

fun ItemCredential.toEntity(): CredentialEntity {
    return CredentialEntity(
        id = id,
        title = title,
        username = username,
        password = password,
        url = url,
        icon = icon,
        notes = notes,
        email = email,
        phoneNumber = phoneNumber
    )
}

fun List<CredentialEntity>.toModelList(): List<ItemCredential> {
    return this.map { it.toModel() }
}