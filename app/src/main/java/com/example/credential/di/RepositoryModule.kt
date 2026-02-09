package com.example.credential.di

import com.example.credential.data.CredentialRepository
import com.example.credential.database.dao.CredentialDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(credentialDao: CredentialDao) : CredentialRepository {
        return CredentialRepository(credentialDao)
    }
}