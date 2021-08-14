package com.alva.testbrowser.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class PhotoViewModel : ViewModel() {
    val photoList = liveData { emit(JavascriptInterface.imageUrls) }
}