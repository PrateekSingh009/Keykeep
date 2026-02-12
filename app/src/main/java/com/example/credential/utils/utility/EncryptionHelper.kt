package com.example.credential.utils.utility

import android.content.Context
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptionHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val MASTER_KEY_ALIAS = "credential_master_key"
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val TRANSFORMATION = "AES/GCM/NoPadding"
    private val GCM_IV_LENGTH = 12
    private val GCM_TAG_LENGTH = 128

    private val masterKey by lazy {
        MasterKey.Builder(context, MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""

        val key = getSecretKey(masterKey)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // Combine: IV (12 bytes) + ciphertext + authentication tag
        val combined = iv + encryptedBytes

        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(encryptedBase64: String): String {
        if (encryptedBase64.isEmpty()) return ""

        val key = getSecretKey(masterKey)

        val combined = Base64.decode(encryptedBase64, Base64.DEFAULT)

        if (combined.size < GCM_IV_LENGTH) return ""

        val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
        val encryptedData = combined.copyOfRange(GCM_IV_LENGTH, combined.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, spec)

        val decryptedBytes = cipher.doFinal(encryptedData)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun getSecretKey(masterKey: MasterKey): java.security.Key {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val entry = keyStore.getEntry(MASTER_KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return entry?.secretKey ?: throw IllegalStateException("Master key not found")
    }
}