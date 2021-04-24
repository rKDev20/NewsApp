package com.sample.newsapp.components

import com.sample.newsapp.modules.ApiEndpointModule
import com.sample.newsapp.modules.NewsAppModule
import com.sample.newsapp.tests.LocalNewsSourceTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsAppModule::class, ApiEndpointModule::class])
abstract class NewsAppComponentTest : NewsAppComponent() {
    abstract fun inject(localNewsSourceTest: LocalNewsSourceTest)
}