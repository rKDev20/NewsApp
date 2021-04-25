package com.sample.newsapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsModel(
    @PrimaryKey val title: String,
    @Embedded var source: NewsSource,
    val author: String?,
    val description: String,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class NewsSource(
    @ColumnInfo(name = "sourceId") val id: String?,
    @ColumnInfo(name = "sourceName") val name: String
)