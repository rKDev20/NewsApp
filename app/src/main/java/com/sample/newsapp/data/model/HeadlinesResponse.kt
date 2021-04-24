package com.sample.newsapp.data.model

data class HeadlinesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsModel>
)
