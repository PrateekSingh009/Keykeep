package com.example.credential.di

import android.content.Context
import androidx.room.Room
import com.example.credential.database.AppDatabase
import com.example.credential.database.dao.CredentialDao
import com.example.credential.utils.utility.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppConstants.CREDENTIAL_DATABASE
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCredentialDao(appDatabase : AppDatabase) : CredentialDao {
        return appDatabase.credentialDao
    }
}