package com.example.credential.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.credential.model.ItemCredential
import com.example.credential.utils.utility.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CredentialViewModel @Inject constructor(private val repository: CredentialRepository) : ViewModel() {

    private val _credentialListLiveData = MutableLiveData<UIState<List<ItemCredential>>>()
    val credentialListLiveData: LiveData<UIState<List<ItemCredential>>>
        get() = _credentialListLiveData

    fun getCredentialListFromDb() {
        _credentialListLiveData.value = UIState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCredentialListFromDb() { _credentialListLiveData.postValue(it) }
        }
    }

    fun upsertCredential(item: ItemCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addDataToDB(item)
        }
    }

    fun deleteCredential(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCredential(id)
        }
    }
}