package com.alva.testbrowser.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebViewModel(application: Application) : AndroidViewModel(application) {
    private val websRepository = WebsRepository(application)

    val allBookmark: LiveData<List<Bookmarktest>> = websRepository.allBookmark

    val allHistory: LiveData<List<Historytest>> = websRepository.allHistory

    fun insertWebs(webs: Bookmarktest) {
        viewModelScope.launch(Dispatchers.IO) { websRepository.insertWebs(webs) }
    }

    fun insertWebs(webs: Historytest) {
        viewModelScope.launch(Dispatchers.IO) { websRepository.insertWebs(webs) }
    }

    fun updateWebs(webs: Bookmarktest) {
        viewModelScope.launch(Dispatchers.IO) { websRepository.updateWebs(webs) }
    }

    fun deleteWebs(webs: Bookmarktest) {
        viewModelScope.launch(Dispatchers.IO) { websRepository.deleteWebs(webs) }
    }

    fun deleteWebs(webs: Historytest) {
        viewModelScope.launch(Dispatchers.IO) { websRepository.deleteWebs(webs) }
    }

    fun deleteAllBookmark() {
        viewModelScope.launch(Dispatchers.IO) { websRepository.deleteAllBookmark() }
    }

    fun deleteAllHistory() {
        viewModelScope.launch { websRepository.deleteAllHistory() }
    }
}