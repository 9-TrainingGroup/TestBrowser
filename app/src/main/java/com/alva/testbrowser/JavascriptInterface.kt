package com.alva.testbrowser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.alva.testbrowser.Activity.PhotoActivity

class JavascriptInterface(private val context: Context) {
    private val imageUrls: ArrayList<String> = ArrayList()

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
        val intent = Intent()
        bundle.putStringArrayList("imageUrls", imageUrls)
        bundle.putInt("index", index)
        intent.putExtra("bundle", bundle)
        intent.setClass(context, PhotoActivity::class.java)
        context.startActivity(intent)
    }
}