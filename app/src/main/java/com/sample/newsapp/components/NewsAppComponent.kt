package com.sample.newsapp.components

import android.content.Context
import com.sample.newsapp.data.source.NewsRepository
import com.sample.newsapp.modules.ApiEndpointModule
import com.sample.newsapp.modules.NewsAppModule
import dagger.BindsInstance
import dagger.Component
import java.text.SimpleDateFormat
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsAppModule::class, ApiEndpointModule::class])
abstract class NewsAppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Context): Builder

        fun build(): NewsAppComponent
    }

    abstract fun getNewsRepository(): NewsRepository

    abstract fun getDateTimeFormatter(): SimpleDateFormat
}