package com.example.credential.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemCredential(
    val id:Int = 0,
    val title: String,
    val username: String,
    val password: String,
    val url: String?,
    val icon: String?,
    val notes: String?,
    val email: String?,
    val phoneNumber: String?,
    val category: String? = null
) : Parcelable
