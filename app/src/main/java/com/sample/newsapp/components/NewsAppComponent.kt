package com.sample.newsapp.components

import android.content.Context
import com.sample.newsapp.data.source.NewsRepository
import com.sample.newsapp.modules.ApiEndpointModule
import com.sample.newsapp.modules.NewsAppModule
import dagger.Component
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsAppModule::class, ApiEndpointModule::class])
abstract class NewsAppComponent {
    abstract fun getApplication(): Context

    abstract fun getNewsRepository(): NewsRepository

    abstract fun getDateTimeFormatter(): SimpleDateFormat
}