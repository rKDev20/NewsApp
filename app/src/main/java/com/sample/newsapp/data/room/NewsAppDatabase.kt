package com.sample.newsapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.data.room.dao.NewsDao

@Database(entities = [NewsModel::class], version = 1)
abstract class NewsAppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}