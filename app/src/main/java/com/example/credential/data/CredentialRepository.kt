package com.example.credential.data

import android.content.Context
import android.util.Log
import com.example.credential.database.dao.CredentialDao
import com.example.credential.model.ItemCredential
import com.example.credential.utils.extensions.toEntity
import com.example.credential.utils.extensions.toModelList
import com.example.credential.utils.utility.EncryptionHelper
import com.example.credential.utils.utility.UIState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialRepository @Inject constructor(
    val dao: CredentialDao,
    private val encryptionHelper: EncryptionHelper
) {

    suspend fun getCredentialListFromDb(result: (UIState<List<ItemCredential>>) -> Unit) {
        val list = dao.getAllCredentials().toModelList().map {
            it.copy(
                password = try {
                    encryptionHelper.decrypt(it.password)
                } catch (e: Exception) {
                    ""
                }
            )

        }
        result.invoke(
            UIState.Success(list)
        )
    }

    suspend fun deleteCredential(id: Int) {
        dao.deleteCredentials(id)
    }

    suspend fun addDataToDB(item: ItemCredential) {
        val encryptedItem = item.copy(
            password = encryptionHelper.encrypt(item.password)
        )
        dao.upsert(encryptedItem.toEntity())
    }
}