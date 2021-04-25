package com.sample.newsapp.data.source

import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.qualifiers.NewsStaleTime
import io.reactivex.Completable
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    var localNewsSource: LocalNewsSource,
    var apiNewsSource: ApiNewsSource,
    var dateFormat: SimpleDateFormat,
    @NewsStaleTime val staleTime: Int
) {

    fun fetchCachedNews(page: Int): Single<List<NewsModel>> {
        return localNewsSource.getNews(page)
    }

    private fun fetchAllCachedNews(): Single<List<NewsModel>> {
        return localNewsSource.getNews()
    }

    fun fetchLatestNews(page: Int): Single<List<NewsModel>> {
        return apiNewsSource.getNews(page)
            .map { list -> filter(list) }
            .zipWith(
                fetchAllCachedNews(),
                { t1, t2 -> removeDuplicates(t1, t2) }
            )
            .map { list -> sortList(list) }
            .flatMap { cacheLatestNews(it).toSingleDefault(it) }
    }

    private fun filter(list: List<NewsModel>): List<NewsModel> {
        val resultList: MutableList<NewsModel> = mutableListOf()
        val time = System.currentTimeMillis()
        val staleTimeInMillis = TimeUnit.MILLISECONDS.convert(
            staleTime.toLong(),
            TimeUnit.DAYS
        )
        for (model in list) {
            try {
                dateFormat.parse(model.publishedAt)?.let {
                    if (time - it.time <= staleTimeInMillis)
                        resultList.add(model)
                }
            } catch (ignore: Exception) {
            }
        }
        return resultList
    }

    private fun sortList(list: List<NewsModel>): List<NewsModel> {
        return list.sortedWith { o1, o2 ->
            if (dateFormat.parse(o1.publishedAt)
                    ?.before(dateFormat.parse(o2.publishedAt)) == true
            ) 1
            else -1
        }
    }

    private fun removeDuplicates(src: List<NewsModel>, existing: List<NewsModel>): List<NewsModel> {
        val resultList: MutableList<NewsModel> = mutableListOf()
        for (srcModel in src) {
            var found = false
            for (existingModel in existing) {
                if (srcModel.title == existingModel.title) {
                    found = true
                    break
                }
            }
            if (!found) resultList.add(srcModel)
        }
        return resultList
    }

    private fun cacheLatestNews(news: List<NewsModel>): Completable {
        return localNewsSource.insertNews(news)
    }
}