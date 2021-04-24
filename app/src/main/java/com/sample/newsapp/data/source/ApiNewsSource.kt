package com.sample.newsapp.data.source

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.sample.newsapp.BuildConfig
import com.sample.newsapp.data.interfaces.NewsSource
import com.sample.newsapp.data.model.HeadlinesResponse
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.qualifiers.HeadlinesUrl
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiNewsSource @Inject constructor(@HeadlinesUrl private val headlinesUrl: String) :
    NewsSource {
    override fun getNews(page: Int): Single<List<NewsModel>> {
        return Rx2AndroidNetworking.get(headlinesUrl)
            .addQueryParameter("apiKey", BuildConfig.NEWS_API_KEY)
            .addQueryParameter("page", page.toString())
            .addQueryParameter("pageSize", "20")
            .addQueryParameter("country", "in")
            .build()
            .getObjectSingle(HeadlinesResponse::class.java)
            .map { it.articles }
    }
}