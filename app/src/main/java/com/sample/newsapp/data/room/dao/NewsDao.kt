package com.sample.newsapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.newsapp.data.model.NewsModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface NewsDao {
    @Query("SELECT * FROM NewsModel ORDER BY datetime(publishedAt) DESC LIMIT :lowerLimit, :upperLimit")
    fun getNews(lowerLimit: Int, upperLimit: Int): Single<List<NewsModel>>

    @Query("DELETE FROM NewsModel WHERE datetime(publishedAt) <= datetime('now', 'localtime', '-1 days')")
    fun deleteOldNews(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(news: List<NewsModel>): Completable
}