package com.sample.newsapp.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NewsAppModule(private val application: Application) {

    @Singleton
    @Provides
    fun providesApplication(): Application {
        return application
    }
}