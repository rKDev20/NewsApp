package com.sample.newsapp.components

import android.app.Application
import com.sample.newsapp.modules.NewsAppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsAppModule::class])
abstract class NewsAppComponent {
    abstract fun getApplication(): Application
}