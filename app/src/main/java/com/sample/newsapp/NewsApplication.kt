package com.sample.newsapp

import androidx.multidex.MultiDexApplication
import com.sample.newsapp.components.DaggerNewsAppComponent
import com.sample.newsapp.components.NewsAppComponent
import io.reactivex.plugins.RxJavaPlugins

open class NewsApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        component = getAppComponent()
        RxJavaPlugins.setErrorHandler { it?.cause?.printStackTrace() }
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