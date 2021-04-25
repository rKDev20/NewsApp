package com.sample.newsapp.data.source

import com.sample.newsapp.data.model.NewsModel
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    var localNewsSource: LocalNewsSource,
    var apiNewsSource: ApiNewsSource
) {

    fun fetchCachedNews(page: Int): Single<List<NewsModel>> {
        return localNewsSource.getNews(page)
    }

    fun fetchLatestNews(page: Int): Single<List<NewsModel>> {
        return apiNewsSource.getNews(page)
            .flatMap { cacheLatestNews(it).toSingleDefault(it) }
    }

    private fun cacheLatestNews(news: List<NewsModel>): Completable {
        return localNewsSource.insertNews(news)
    }
}