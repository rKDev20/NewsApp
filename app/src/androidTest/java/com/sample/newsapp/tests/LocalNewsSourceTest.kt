package com.sample.newsapp.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sample.newsapp.components.DaggerNewsAppComponentTest
import com.sample.newsapp.components.NewsAppComponentTest
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.data.model.NewsSource
import com.sample.newsapp.data.source.ApiNewsSource
import com.sample.newsapp.data.source.LocalNewsSource
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class LocalNewsSourceTest {

    @Inject
    lateinit var localNewsSource: LocalNewsSource

    @Inject
    lateinit var apiNewsSource: ApiNewsSource

    @Inject
    lateinit var dateFormatter: SimpleDateFormat

    lateinit var newsAppComponent: NewsAppComponentTest

    @Test
    fun verifyNewsDao() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        newsAppComponent = DaggerNewsAppComponentTest.builder()
            .application(appContext)
            .build()
        newsAppComponent.inject(this)
        val newsModel1 = NewsModel(
            "title1",
            NewsSource(null, "newsSource1"),
            "author1",
            "description1",
            "url1",
            "imageUrl1",
            dateFormatter.format(Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000)),//10 hours old news
            "content1"
        )
        val newsModel2 = NewsModel(
            "title2",
            NewsSource(null, "newsSource2"),
            "author2",
            "description2",
            "url2",
            "imageUrl2",
            dateFormatter.format(Date(System.currentTimeMillis() - 5 * 60 * 60 * 1000)),//5 hours old news
            "content2"
        )
        val newsModel3 = NewsModel(
            "title3",
            NewsSource(null, "newsSource3"),
            "author3",
            "description3",
            "url3",
            "imageUrl3",
            dateFormatter.format(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)),//1 day old news
            "content3"
        )
        val sampleDataList: List<NewsModel> = listOf(
            newsModel1,
            newsModel2,
            newsModel3
        )
        localNewsSource.insertNews(sampleDataList).subscribe()
        val listFromDatabase = localNewsSource.getNews(0).blockingGet()
        //Verifies that expired news is deleted
        assertEquals(2, listFromDatabase.size)
        //Verifies correct news in correct order is fetched
        assertEquals(newsModel2, listFromDatabase[0])
        assertEquals(newsModel1, listFromDatabase[1])
    }
}