package com.sample.newsapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.newsapp.adapters.NewsAdapter
import com.sample.newsapp.viewmodel.NewsViewModel
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NewsAdapter
    private val disposable = CompositeDisposable()

    private lateinit var recyclerView: RecyclerView
    private lateinit var newDataAvailability: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val model: NewsViewModel by viewModels()

        recyclerView = findViewById(R.id.recyclerView)
        newDataAvailability = findViewById(R.id.newDataAvailability)

        adapter = NewsAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        disposable.add(
            model.dataStream.subscribe { adapter.dataList = it }
        )

        disposable.add(
            model.newDataAvailability.subscribe { isAvailable ->
                newDataAvailability.visibility =
                    if (isAvailable) View.VISIBLE
                    else View.GONE
            }
        )

        newDataAvailability.setOnClickListener { model.switchToLatestNews() }

    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}