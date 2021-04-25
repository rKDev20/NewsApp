package com.sample.newsapp.components

import android.content.Context
import com.sample.newsapp.modules.ApiEndpointModule
import com.sample.newsapp.modules.NewsAppModule
import com.sample.newsapp.tests.LocalNewsSourceTest
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsAppModule::class, ApiEndpointModule::class])
abstract class NewsAppComponentTest : NewsAppComponent() {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Context): Builder

        fun build(): NewsAppComponentTest
    }

    abstract fun inject(localNewsSourceTest: LocalNewsSourceTest)
}