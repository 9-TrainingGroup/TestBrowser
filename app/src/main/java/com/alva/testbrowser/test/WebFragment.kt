package com.alva.testbrowser.test

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.FragmentWebBinding
import com.alva.testbrowser.webview.*

class WebFragment : Fragment() {
    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webViewPool = WebViewPool()
        WebViewPool.init(requireContext())
        val webView: WebViewExt = webViewPool.getWebView(requireContext())
        webView.init()
        webView.loadUrl(arguments?.getString("NEWS_POSITION").toString())
        binding.webView.addView(webView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebViewExt.init() {
    val progressView = ProgressView(context)
    progressView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp4px(context, 4F)
    )
    progressView.setColor(R.color.cyan)
    progressView.setProgress(10)
    addView(progressView)

    val settings = settings
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.cacheMode = WebSettings.LOAD_DEFAULT
    settings.domStorageEnabled = true
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true
    settings.builtInZoomControls = true
    settings.setSupportZoom(true)

    webViewClient = WebClient()
    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressView.setProgress(newProgress)
            progressView.isVisible = newProgress != 100
        }
    }
}

fun dp4px(context: Context, dp: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}
