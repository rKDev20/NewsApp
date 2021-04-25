package com.sample.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.sample.newsapp.NewsApplication
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.data.source.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class NewsViewModel : ViewModel() {

    @Inject
    lateinit var repository: NewsRepository

    companion object {
        const val MODE_OFFLINE = 0
        const val MODE_ONLINE = 1
    }

    private val disposable = CompositeDisposable()
    private var mode = MODE_OFFLINE

    private val offlineDataList: MutableList<NewsModel> = mutableListOf()
    private val onlineDataList: MutableList<NewsModel> = mutableListOf()

    private var offlinePageNo = 0
    private var onlinePageNo = 0

    private var isOfflineDataInFlight = false
    private var isOnlineDataInFlight = false

    val dataStream: BehaviorSubject<List<NewsModel>> = BehaviorSubject.create()
    val newDataAvailability: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val state: BehaviorSubject<NewsState> =
        BehaviorSubject.createDefault(NewsState(State.STATE_LOADING, "Loading news..."))

    init {
        NewsApplication.component.inject(this)
        loadCachedNews()
        loadLatestNews()
    }

    private fun loadCachedNews(explicit: Boolean = false) {
        isOfflineDataInFlight = true
        if (explicit) state.onNext(NewsState(State.STATE_LOADING, "Loading news..."))
        disposable.add(
            repository.fetchCachedNews(offlinePageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list ->
                    isOfflineDataInFlight = false
                    if (list.isNotEmpty() && mode == MODE_OFFLINE) {
                        offlineDataList.addAll(list)
                        offlinePageNo++
                        dispatchNewData()
                    }
                }
        )
    }

    private fun loadLatestNews() {
        isOnlineDataInFlight = true
        disposable.add(
            repository.fetchLatestNews(onlinePageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { list ->
                        isOnlineDataInFlight = false
                        if (list.isNotEmpty()) {
                            onlineDataList.addAll(list)
                            onlinePageNo++
                            if (offlineDataList.isNotEmpty() && mode == MODE_OFFLINE)
                                newDataAvailability.onNext(true)
                            else {
                                mode = MODE_ONLINE
                                dispatchNewData()
                            }
                        }
                    },
                    {
                        isOnlineDataInFlight = false
                        if (offlineDataList.isEmpty() && onlineDataList.isEmpty())
                            state.onNext(NewsState(State.STATE_ERROR, "Failed to load news"))
                    }
                )
        )
    }

    private fun dispatchNewData() {
        state.onNext(NewsState(State.STATE_AVAILABLE))
        dataStream.onNext(
            if (mode == MODE_OFFLINE)
                offlineDataList
            else onlineDataList
        )
    }

    fun switchToLatestNews() {
        if (mode == MODE_OFFLINE) {
            mode = MODE_ONLINE
            newDataAvailability.onNext(false)
            dispatchNewData()
        }
    }

    fun loadNextPage() {
        if (mode == MODE_OFFLINE && !isOfflineDataInFlight)
            loadCachedNews()
        else if (mode == MODE_ONLINE && !isOnlineDataInFlight)
            loadLatestNews()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun retry() {
        if (offlineDataList.isEmpty()
            && onlineDataList.isEmpty()
            && !isOnlineDataInFlight
            && !isOfflineDataInFlight
        ) {
            loadCachedNews(true)
        }
    }

    data class NewsState(
        val state: State,
        val message: String = ""
    )

    enum class State {
        STATE_LOADING,
        STATE_AVAILABLE,
        STATE_ERROR
    }

}