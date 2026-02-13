package com.example.credential.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("categories")
class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String?,
    val count: Int
)