package com.example.credential.data

import com.example.credential.database.dao.CredentialDao
import com.example.credential.model.ItemCredential
import com.example.credential.utils.extensions.toEntity
import com.example.credential.utils.extensions.toModelList
import com.example.credential.utils.utility.UIState
import javax.inject.Inject

class CredentialRepository @Inject constructor(val dao: CredentialDao) {

    suspend fun getCredentialListFromDb(result: (UIState<List<ItemCredential>>) -> Unit){
        result.invoke(
            UIState.Success(dao.getAllCredentials().toModelList())
        )
    }

    suspend fun deleteCredential(id: Int) {
        dao.deleteCredentials(id)
    }

    suspend fun addDataToDB(item: ItemCredential) {
        dao.upsert(item.toEntity())
    }
}