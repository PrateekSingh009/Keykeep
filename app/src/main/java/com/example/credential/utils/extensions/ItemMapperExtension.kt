package com.example.credential.utils.extensions

import com.example.credential.database.entity.CategoryEntity
import com.example.credential.database.entity.CredentialEntity
import com.example.credential.model.ItemCategory
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

fun List<CredentialEntity>.toCredentialModelList(): List<ItemCredential> {
    return this.map { it.toModel() }
}

fun CategoryEntity.toModel(): ItemCategory {
    return ItemCategory(
        id = id,
        name = name,
        icon = icon,
        count = count
    )
}

fun ItemCategory.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        count = count
    )
}

fun List<CategoryEntity>.toCategoryModelList(): List<ItemCategory> {
    return this.map { it.toModel() }
}