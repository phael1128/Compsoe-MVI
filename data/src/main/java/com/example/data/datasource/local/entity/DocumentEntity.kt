package com.example.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "searching_table",
    indices = [Index(value = ["url"], unique = true)]
)
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "collection") val collection: String?,
    @ColumnInfo(name = "thumbnailUrl") val thumbnailUrl: String?,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?,
    @ColumnInfo(name = "width") val width: Long?,
    @ColumnInfo(name = "height") val height: Long?,
    @ColumnInfo(name = "displaySiteName") val displaySiteName: String?,
    @ColumnInfo(name = "docUrl") val docUrl: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "playTime") val playTime: Int?,
    @ColumnInfo(name = "thumbnail") val thumbnail: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "datetime") val datetime: String,
)
