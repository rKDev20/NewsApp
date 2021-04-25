package com.sample.newsapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sample.newsapp.databinding.FragmentWebViewBinding
import com.sample.newsapp.viewmodel.NewsViewModel

class WebViewFragment : Fragment() {
    companion object {
        fun getBundle(url: String): Bundle {
            val bundle = Bundle()
            bundle.putString("url", url)
            return bundle
        }

        fun getUrl(bundle: Bundle?): String {
            return bundle?.getString("url", "") ?: ""
        }
    }

    private lateinit var binding: FragmentWebViewBinding
    private lateinit var model: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebViewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = ViewModelProvider(requireActivity()).get(NewsViewModel::class.java)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = getUrl(arguments)
        binding.webView.apply {

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    requestedUrl: String
                ): Boolean {
                    if (requestedUrl == url) {
                        view.loadUrl(requestedUrl)
                        return true
                    }
                    return false
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                }

                @Suppress("DEPRECATION")
                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    model.webViewDestroyed()
                }

                override fun onRenderProcessGone(
                    view: WebView?,
                    detail: RenderProcessGoneDetail?
                ): Boolean {
                    model.webViewDestroyed()
                    view?.destroy()
                    return true
                }
            }

            loadUrl(url)
        }
    }
}