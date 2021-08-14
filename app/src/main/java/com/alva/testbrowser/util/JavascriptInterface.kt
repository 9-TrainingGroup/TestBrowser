package com.alva.testbrowser.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import com.alva.testbrowser.activity.PhotoActivity

class JavascriptInterface(private val context: Context) {
    companion object {
        val imageUrls = ArrayList<String>()
        var index: Int = 0
    }

    @JavascriptInterface
    fun clearImageUrl() {
        imageUrls.clear()
    }

    @JavascriptInterface
    fun getImageUrl(url: String) {
        imageUrls.add(url)
    }

    @JavascriptInterface
    fun openImage(url: String) {
        index = imageUrls.indexOf(url)
        context.startActivity(
            Intent(context, PhotoActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(context as Activity).toBundle()
        )
    }
}