package com.sample.newsapp.modules

import com.sample.newsapp.qualifiers.HeadlinesUrl
import dagger.Module
import dagger.Provides

@Module
class ApiEndpointModule {
    private val baseUrl = "https://newsapi.org/v2"

    @HeadlinesUrl
    @Provides
    fun getHeadlinesUrl(): String {
        return "$baseUrl/top-headlines"
    }
}