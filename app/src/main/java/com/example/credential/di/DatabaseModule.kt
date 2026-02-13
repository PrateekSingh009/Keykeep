package com.example.credential.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
//            .addMigrations(MIGRATION_2_3) // Add this in the Production Phase
            .build()
    }

    @Provides
    @Singleton
    fun provideCredentialDao(appDatabase : AppDatabase) : CredentialDao {
        return appDatabase.credentialDao
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE credentials ADD COLUMN category TEXT DEFAULT 'Others'")
        }
    }
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
            CREATE TABLE IF NOT EXISTS Category (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                categoryName TEXT NOT NULL
            )
        """.trimIndent())
        }
    }
}