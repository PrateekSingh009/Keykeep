package com.example.credential.data

import android.content.Context
import android.util.Log
import com.example.credential.database.dao.CredentialDao
import com.example.credential.model.ItemCategory
import com.example.credential.model.ItemCredential
import com.example.credential.utils.extensions.processList
import com.example.credential.utils.extensions.toCategoryModelList
import com.example.credential.utils.extensions.toEntity
import com.example.credential.utils.extensions.toCredentialModelList
import com.example.credential.utils.utility.EncryptionHelper
import com.example.credential.utils.utility.UIState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialRepository @Inject constructor(
    val dao: CredentialDao,
    private val encryptionHelper: EncryptionHelper
) {

    suspend fun getCredentialsList(categoryId: Int? = null, result: (UIState<List<ItemCredential>>) -> Unit) {
        val rawList = if (categoryId == null || categoryId == 0) {
            dao.getAllCredentials()
        } else {
            dao.getCredentialByCategoryId(categoryId)
        }

        result.invoke(UIState.Success(rawList.processList(encryptionHelper)))
    }

    suspend fun getCategoryListFromDb(result: (UIState<List<ItemCategory>>) -> Unit) {
        result.invoke(
            UIState.Success(dao.getAllCategory().toCategoryModelList())
        )
    }

    suspend fun deleteCredential(id: Int) {
        val credentialToDelete = dao.getById(id)

        credentialToDelete?.categoryId?.let { catId ->
            dao.decrementCategoryCount(catId)
        }
        dao.deleteCredentials(id)
    }

    suspend fun addDataToDB(newCredential: ItemCredential) {

        val oldCredential = if (newCredential.id != 0) {
            dao.getById(newCredential.toEntity().id)
        } else {
            null
        }

        val oldCatId = oldCredential?.categoryId
        val newCatId = newCredential.categoryId

        if (oldCatId != newCatId) {
            oldCatId?.let { dao.decrementCategoryCount(it) }
            newCatId?.let { dao.incrementCategoryCount(it) }
        }
        val encryptedItem = newCredential.copy(
            password = encryptionHelper.encrypt(newCredential.password)
        )
        dao.upsert(encryptedItem.toEntity())
    }

    suspend fun addCategoryToDB(item: ItemCategory) {
        dao.upsert(item.toEntity())
    }
}