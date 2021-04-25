package com.sample.newsapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sample.newsapp.data.model.NewsModel
import com.sample.newsapp.databinding.ActivityMainBinding
import com.sample.newsapp.fragments.HomeFragment
import com.sample.newsapp.fragments.WebViewFragment
import com.sample.newsapp.viewmodel.NewsViewModel
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private val model: NewsViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val disposable = CompositeDisposable()

    private var isHomeFragmentActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openHomeFragment()
        disposable.addAll(
            model.expandNewListener.subscribe { openWebViewFragment(it) },
            model.webviewCrashedListener.subscribe {
                Toast.makeText(this, "Couldn't load news!", Toast.LENGTH_SHORT).show()
                openHomeFragment()
            }
        )
    }

    private fun openHomeFragment() {
        isHomeFragmentActive = true
        binding.title.apply {
            text = getString(R.string.app_name)
            visibility = View.VISIBLE
        }
        binding.webViewTitle.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, homeFragment, "home")
            .commit()
    }

    private fun openWebViewFragment(newsModel: NewsModel) {
        isHomeFragmentActive = false
        newsModel.url?.let {
            binding.webViewTitle.apply {
                text = newsModel.title
                visibility = View.VISIBLE
            }
            binding.title.visibility = View.GONE
            val fragment = WebViewFragment()
            fragment.arguments = WebViewFragment.getBundle(it)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, "webview")
                .commit()
        } ?: Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (isHomeFragmentActive)
            super.onBackPressed()
        else openHomeFragment()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

}