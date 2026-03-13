package com.example.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.datasource.local.entity.DocumentEntity

@Dao
interface SearchingDao {
    @Query("SELECT * FROM searching_table")
    suspend fun getSavedSearchingData() : List<DocumentEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSearchingEntity(documentEntity: DocumentEntity)

    @Query(
        """
        DELETE FROM searching_table
        WHERE (:docUrl IS NOT NULL AND docUrl = :docUrl)
           OR (:url IS NOT NULL AND url = :url)
        """,
    )
    suspend fun deleteSearchingEntity(
        docUrl: String?,
        url: String?,
    )
}
