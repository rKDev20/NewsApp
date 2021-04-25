package com.sample.newsapp.modules

import com.sample.newsapp.qualifiers.NewsStaleTime
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
class NewsAppModule {

    @Singleton
    @Provides
    fun getDateTimeFormatter() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    @NewsStaleTime
    @Singleton
    @Provides
    fun getStaleTimeForNews() = 3
}