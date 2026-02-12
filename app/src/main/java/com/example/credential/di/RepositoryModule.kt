package com.example.credential.di

import android.content.Context
import com.example.credential.data.CredentialRepository
import com.example.credential.database.dao.CredentialDao
import com.example.credential.utils.utility.EncryptionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(credentialDao: CredentialDao,encryptionHelper: EncryptionHelper) : CredentialRepository {
        return CredentialRepository(credentialDao,encryptionHelper)
    }
}