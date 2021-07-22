package com.alva.testbrowser.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    val photoList: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
}