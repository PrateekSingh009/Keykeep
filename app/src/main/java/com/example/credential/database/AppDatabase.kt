package com.example.credential.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.credential.database.dao.CredentialDao
import com.example.credential.database.entity.CredentialEntity

@Database(entities = [CredentialEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val credentialDao : CredentialDao
}