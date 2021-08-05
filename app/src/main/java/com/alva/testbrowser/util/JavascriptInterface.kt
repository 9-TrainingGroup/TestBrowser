package com.alva.testbrowser.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.alva.testbrowser.activity.PhotoActivity

class JavascriptInterface(private val context: Context) {
    private val imageUrls = ArrayList<String>()

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
        val index = imageUrls.indexOf(url)
        val bundle = Bundle()
        val intent = Intent(context, PhotoActivity::class.java)
        bundle.putStringArrayList("imageUrls", imageUrls)
        bundle.putInt("index", index)
        intent.putExtra("bundle", bundle)
        context.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(context as Activity).toBundle()
        )
    }
}