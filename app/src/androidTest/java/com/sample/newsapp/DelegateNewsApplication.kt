package com.sample.newsapp

import com.sample.newsapp.components.DaggerNewsAppComponent
import com.sample.newsapp.components.NewsAppComponent
import com.sample.newsapp.modules.NewsAppModule

class DelegateNewsApplication : NewsApplication() {
    override fun getAppComponent(): NewsAppComponent {
        return DaggerNewsAppComponent.builder()
            .newsAppModule(NewsAppModule(this))
            .build()
    }
}