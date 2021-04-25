package com.sample.newsapp

import com.sample.newsapp.components.DaggerNewsAppComponent
import com.sample.newsapp.components.NewsAppComponent

class DelegateNewsApplication : NewsApplication() {
    override fun getAppComponent(): NewsAppComponent {
        return DaggerNewsAppComponent.builder()
            .application(this)
            .build()
    }
}