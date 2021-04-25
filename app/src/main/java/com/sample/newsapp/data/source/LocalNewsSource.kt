package com.sample.newsapp.data.source

import android.content.Context
import androidx.room.Room
import com.sample.newsapp.data.interfaces.NewsSource
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.data.room.NewsAppDatabase
import com.sample.newsapp.qualifiers.NewsStaleTime
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalNewsSource @Inject constructor(context: Context, @NewsStaleTime val staleTime: Int) :
    NewsSource {
    private val database =
        Room.databaseBuilder(context, NewsAppDatabase::class.java, "news-app").build()
    private val newsDao = database.newsDao()

    override fun getNews(page: Int): Single<List<NewsModel>> {
        val lowerLimit = page * 20
        val upperLimit = lowerLimit + 20
        return newsDao.deleteOldNews(staleTime)
            .andThen(newsDao.getNews(lowerLimit, upperLimit))
    }

    fun getNews(): Single<List<NewsModel>> {
        return newsDao.deleteOldNews(staleTime)
            .andThen(newsDao.getNews())
    }

    fun insertNews(news: List<NewsModel>): Completable {
        return newsDao.insertOrUpdate(news)
    }
}