package com.sample.newsapp.data.interfaces

import com.sample.newsapp.data.model.NewsModel
import io.reactivex.Single

interface NewsSource {
    fun getNews(page: Int): Single<List<NewsModel>>
}