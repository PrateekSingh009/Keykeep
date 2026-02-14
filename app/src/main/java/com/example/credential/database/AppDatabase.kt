package com.example.credential.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.credential.database.dao.CredentialDao
import com.example.credential.database.entity.CategoryEntity
import com.example.credential.database.entity.CredentialEntity

@Database(entities = [CredentialEntity::class,CategoryEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract val credentialDao : CredentialDao
}