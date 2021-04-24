package com.sample.newsapp.data.source

import com.sample.newsapp.data.model.NewsModel
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
            .doAfterSuccess { cacheLatestNews(it) }
    }

    fun fetchLatestNews(page: Int): Single<List<NewsModel>> {
        return apiNewsSource.getNews(page)
    }

    private fun cacheLatestNews(news: List<NewsModel>) {
        localNewsSource.insertNews(news)
    }
}