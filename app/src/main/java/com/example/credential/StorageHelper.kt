package com.example.credential

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.credential.model.ItemCredential

object StorageHelper {
    private const val PREF_NAME = "credentials_prefs"
    private const val SERVICES_KEY = "services"

    private fun getPrefs(context: Context): EncryptedSharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    fun saveCredential(context: Context, service: String, username: String, password: String) {
        val prefs = getPrefs(context)
        with(prefs.edit()) {
            putString("${service}_username", username)
            putString("${service}_password", password)

            val services = getServices(context).toMutableSet()
            services.add(service)
            putStringSet(SERVICES_KEY, services)
            apply()
        }
    }

//    fun getAllCredentials(context: Context): List<ItemCredential> {
//        val prefs = getPrefs(context)
//        val services = prefs.getStringSet(SERVICES_KEY, emptySet()) ?: emptySet()
//        return services.map { service ->
//            val username = prefs.getString("${service}_username", "") ?: ""
//            val password = prefs.getString("${service}_password", "") ?: ""
//            ItemCredential(service, username, password,"","","","","")
//        }
//    }

    fun getServices(context: Context): Set<String> {
        return getPrefs(context).getStringSet(SERVICES_KEY, emptySet()) ?: emptySet()
    }
}