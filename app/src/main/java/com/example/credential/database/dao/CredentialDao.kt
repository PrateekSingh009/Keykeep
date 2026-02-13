package com.example.credential.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.credential.database.entity.CategoryEntity
import com.example.credential.database.entity.CredentialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao {

    @Upsert
    suspend fun upsert(credential: CredentialEntity)
//
    @Query("SELECT * FROM credentials ORDER BY title ASC")
    suspend fun getAllCredentials(): List<CredentialEntity>   // Use Flow
//
//    @Query("SELECT * FROM credentials WHERE title = :title LIMIT 1")
//    suspend fun getCredentialByService(title: String): CredentialEntity?

    @Query("SELECT * FROM categories ORDER BY name ASC")
    suspend fun getAllCategory(): List<CategoryEntity>

    @Query("DELETE FROM credentials WHERE id = :id")
    suspend fun deleteCredentials(id: Int)

    @Upsert
    suspend fun upsert(category: CategoryEntity)
}