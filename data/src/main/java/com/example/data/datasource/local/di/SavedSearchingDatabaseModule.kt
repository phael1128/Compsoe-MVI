package com.example.data.datasource.local.di

import android.content.Context
import androidx.room.migration.Migration
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.datasource.local.database.SearchingResultDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedSearchingDatabaseModule {

    @Provides
    @Singleton
    fun provideSearchingResultDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        SearchingResultDataBase::class.java,
        "searching-result-database"
    ).addMigrations(MIGRATION_1_2).build()

    @Provides
    @Singleton
    fun provideSearchingDao(
        searchingResultDataBase: SearchingResultDataBase
    ) = searchingResultDataBase.searchingDao()

    private val MIGRATION_1_2 =
        object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE searching_table ADD COLUMN searchingViewType TEXT")
                database.execSQL(
                    """
                    UPDATE searching_table
                    SET searchingViewType = CASE
                        WHEN docUrl IS NOT NULL AND docUrl != '' THEN 'Image'
                        ELSE 'Video'
                    END
                    """.trimIndent(),
                )
            }
        }
}
