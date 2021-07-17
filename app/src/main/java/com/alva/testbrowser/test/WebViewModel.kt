package com.alva.testbrowser.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WebViewModel(application: Application) : AndroidViewModel(application) {
    private val websRepository = WebsRepository(application)

    val allBookmark: LiveData<List<Bookmark>> = websRepository.allBookmark

    val allHistory: LiveData<List<History>> = websRepository.allHistory

    fun insertWebs(webs: Bookmark) {
        viewModelScope.launch { websRepository.insertWebs(webs) }
    }

    fun insertWebs(webs: History) {
        viewModelScope.launch { websRepository.insertWebs(webs) }
    }

    fun updateWebs(webs: Bookmark) {
        viewModelScope.launch { websRepository.updateWebs(webs) }
    }

    fun deleteWebs(webs: Bookmark) {
        viewModelScope.launch { websRepository.deleteWebs(webs) }
    }

    fun deleteWebs(webs: History) {
        viewModelScope.launch { websRepository.deleteWebs(webs) }
    }

    fun deleteAllBookmark() {
        viewModelScope.launch { websRepository.deleteAllBookmark() }
    }

    fun deleteAllHistory() {
        viewModelScope.launch { websRepository.deleteAllHistory() }
    }
}