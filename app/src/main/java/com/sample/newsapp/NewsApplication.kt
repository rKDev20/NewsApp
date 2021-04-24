package com.sample.newsapp

import android.app.Application
import com.sample.newsapp.components.DaggerNewsAppComponent
import com.sample.newsapp.components.NewsAppComponent
import com.sample.newsapp.modules.NewsAppModule

class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerNewsAppComponent.builder()
            .newsAppModule(NewsAppModule(this))
            .build()
    }

    companion object{
        lateinit var component: NewsAppComponent
    }
}