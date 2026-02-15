package com.example.credential.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.credential.database.entity.CategoryEntity
import com.example.credential.database.entity.CredentialEntity

@Dao
interface CredentialDao {

    @Upsert
    suspend fun upsert(credential: CredentialEntity)

    @Query("SELECT * FROM credentials ORDER BY title ASC")
    suspend fun getAllCredentials(): List<CredentialEntity>   // Use Flow

    @Query("SELECT * FROM credentials WHERE categoryId = :categoryId")
    suspend fun getCredentialByCategoryId(categoryId: Int): List<CredentialEntity>

    @Query("UPDATE categories SET count = count + 1 WHERE id = :categoryId")
    suspend fun incrementCategoryCount(categoryId: Int)

    @Query("SELECT * FROM credentials WHERE id = :id")
    suspend fun getById(id: Int): CredentialEntity?

    @Query("UPDATE categories SET count = count - 1 WHERE id = :categoryId AND count > 0")
    suspend fun decrementCategoryCount(categoryId: Int)

    @Query("SELECT * FROM categories WHERE id = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: Int): CategoryEntity?

    @Query("SELECT * FROM categories ORDER BY name ASC")
    suspend fun getAllCategory(): List<CategoryEntity>

    @Query("DELETE FROM credentials WHERE id = :id")
    suspend fun deleteCredentials(id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsert(category: CategoryEntity)

    @Query("""
    SELECT * FROM credentials 
    WHERE (title LIKE '%' || :searchQuery || '%' OR username LIKE '%' || :searchQuery || '%')
    ORDER BY title ASC
    """)
    suspend fun searchCredentials(searchQuery: String): List<CredentialEntity>
}