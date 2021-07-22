package com.alva.testbrowser.test

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alva.testbrowser.R
import com.alva.testbrowser.database.Bookmark
import com.alva.testbrowser.database.RecordViewModel
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.alva.testbrowser.databinding.FragmentWebBinding
import com.alva.testbrowser.webview.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        val viewModel by viewModels<RecordViewModel>()
        val webViewPool = WebViewPool()
        val webView: WebViewExt = webViewPool.getWebView(requireContext())
        webView.init()
        webView.loadUrl(arguments?.getString("NEWS_POSITION").toString())
        binding.webView.addView(webView)
        requireActivity().findViewById<ImageButton>(R.id.refreshNews).setOnClickListener {
            val v = View.inflate(it.context, R.layout.dialog_edit_web, null)
            val dialogBinding = DialogEditWebBinding.bind(v)
            dialogBinding.editTextName.setText(webView.title)
            dialogBinding.editTextUrl.setText(webView.url)
            val builder: AlertDialog =
                AlertDialog.Builder(it.context)
                    .setTitle(R.string.dialog_add_title)
                    .setView(v)
                    .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                        viewModel.insertBookmark(
                            Bookmark(
                                dialogBinding.editTextName.text.toString(),
                                dialogBinding.editTextUrl.text.toString()
                            )
                        )
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.dialog_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            dialogBinding.editTextName.requestFocus()
            viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                    dialogBinding.editTextName,
                    0
                )
            }
            dialogBinding.editTextUrl.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        builder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                            setTextColor(Color.BLACK)
                            isEnabled = true
                        }
                    } else {
                        builder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                            setTextColor(Color.GRAY)
                            isEnabled = false
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
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

    webViewClient = object : WebClient() {

    }
    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
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
