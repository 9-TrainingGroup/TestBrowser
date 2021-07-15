package com.alva.testbrowser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    private val bookmarkRepository = BookmarkRepository(application)

    val allWebsLive: LiveData<List<Bookmark>> = bookmarkRepository.allWebsLive
    fun insertWebs(webs: Bookmark) {
        viewModelScope.launch { bookmarkRepository.insertWebs(webs) }
    }

    fun updateWebs(webs: Bookmark) {
        viewModelScope.launch { bookmarkRepository.updateWebs(webs) }
    }

    fun deleteWebs(webs: Bookmark) {
        viewModelScope.launch { bookmarkRepository.deleteWebs(webs) }
    }

    fun deleteAllWebs() {
        viewModelScope.launch { bookmarkRepository.deleteAllWebs() }
    }
}

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyRepository = HistoryRepository(application)

    val allWebsLive: LiveData<List<History>> = historyRepository.allWebsLive
    fun insertWebs(webs: History) {
        viewModelScope.launch { historyRepository.insertWebs(webs) }
    }

    fun deleteWebs(webs: History) {
        viewModelScope.launch { historyRepository.deleteWebs(webs) }
    }

    fun deleteAllWebs() {
        viewModelScope.launch { historyRepository.deleteAllWebs() }
    }
}