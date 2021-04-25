package com.sample.newsapp.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sample.newsapp.adapters.NewsAdapter
import com.sample.newsapp.adapters.NewsClickListener
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.databinding.FragmentHomeBinding
import com.sample.newsapp.viewmodel.NewsViewModel
import io.reactivex.disposables.CompositeDisposable

class HomeFragment : Fragment(), NewsClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: NewsAdapter

    private val disposable = CompositeDisposable()

    private val thresholdPositionToLoadNewData = 5

    private lateinit var model: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        adapter = NewsAdapter(this)
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                LinearLayoutManager(requireContext())
            else StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        var lastPosition = -1
                        if (layoutManager is LinearLayoutManager)
                            lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                        else if (layoutManager is StaggeredGridLayoutManager)
                            lastPosition =
                                layoutManager.findLastCompletelyVisibleItemPositions(null)[0]
                        if (lastPosition < 0)
                            return
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

    override fun onClick(news: NewsModel) {
        model.expandNewListener.onNext(news)
    }
}