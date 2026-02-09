package com.example.credential.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credentials")
data class CredentialEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val username: String,
    val password: String,
    val url: String?,
    val icon: String?,
    val notes: String?,
    val email: String?,
    val phoneNumber: String?
)