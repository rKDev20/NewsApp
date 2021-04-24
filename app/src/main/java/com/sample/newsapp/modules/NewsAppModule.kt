package com.sample.newsapp.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
class NewsAppModule(private val application: Context) {

    @Singleton
    @Provides
    fun providesApplication(): Context {
        return application
    }

    @Singleton
    @Provides
    fun getDateTimeFormatter(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    }

}