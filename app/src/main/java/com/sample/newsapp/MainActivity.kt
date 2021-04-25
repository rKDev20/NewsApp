package com.sample.newsapp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.newsapp.adapters.NewsAdapter
import com.sample.newsapp.databinding.ActivityMainBinding
import com.sample.newsapp.viewmodel.NewsViewModel
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NewsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private val disposable = CompositeDisposable()

    private lateinit var binding: ActivityMainBinding
    private val thresholdPositionToLoadNewData = 5

    private val model: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupStateHandlers()
        setupDataStreamHandler()
        setupNewDataAvailabilityHandler()
        binding.retry.setOnClickListener { model.retry() }
    }

    private fun setupNewDataAvailabilityHandler() {
        disposable.add(
            model.newDataAvailability.subscribe { isAvailable ->
                binding.newDataAvailability.visibility =
                    if (isAvailable) View.VISIBLE
                    else View.GONE
            }
        )
        binding.newDataAvailability.setOnClickListener { model.switchToLatestNews() }
    }

    private fun setupDataStreamHandler() {
        disposable.add(
            model.dataStream.subscribe { adapter.dataList = it }
        )
    }

    private fun setupStateHandlers() {
        disposable.add(
            model.state.subscribe {
                when (it.state) {
                    NewsViewModel.State.STATE_AVAILABLE -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.statusText.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.retry.visibility = View.GONE
                    }
                    NewsViewModel.State.STATE_LOADING -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.statusText.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.retry.visibility = View.GONE
                    }
                    NewsViewModel.State.STATE_ERROR -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.statusText.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.retry.visibility = View.VISIBLE
                    }
                }
                binding.statusText.text = it.message
            }
        )
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter()
        layoutManager = LinearLayoutManager(this)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                        val totalSize = adapter.itemCount
                        if (totalSize - lastPosition <= thresholdPositionToLoadNewData)
                            model.loadNextPage()
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}