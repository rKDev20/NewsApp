package com.sample.newsapp

import androidx.multidex.MultiDexApplication
import com.sample.newsapp.components.DaggerNewsAppComponent
import com.sample.newsapp.components.NewsAppComponent

open class NewsApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        component = getAppComponent()
    }

    protected open fun getAppComponent(): NewsAppComponent {
        return DaggerNewsAppComponent.builder()
            .application(this)
            .build()
    }

    companion object {
        lateinit var component: NewsAppComponent
    }
}