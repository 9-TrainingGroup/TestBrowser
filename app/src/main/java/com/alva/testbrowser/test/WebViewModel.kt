package com.alva.testbrowser.test

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WebViewModel(application: Application) : AndroidViewModel(application) {
    private val websRepository = WebsRepository(application)

    val allBookmark: LiveData<List<Bookmarktest>> = websRepository.allBookmark

    val allHistory: LiveData<List<Historytest>> = websRepository.allHistory

    fun insertWebs(webs: Bookmarktest) {
        viewModelScope.launch { websRepository.insertWebs(webs) }
    }

    fun insertWebs(webs: Historytest) {
        viewModelScope.launch { websRepository.insertWebs(webs) }
    }

    fun updateWebs(webs: Bookmarktest) {
        viewModelScope.launch { websRepository.updateWebs(webs) }
    }

    fun deleteWebs(webs: Bookmarktest) {
        viewModelScope.launch { websRepository.deleteWebs(webs) }
    }

    fun deleteWebs(webs: Historytest) {
        viewModelScope.launch { websRepository.deleteWebs(webs) }
    }

    fun deleteAllBookmark() {
        viewModelScope.launch { websRepository.deleteAllBookmark() }
    }

    fun deleteAllHistory() {
        viewModelScope.launch { websRepository.deleteAllHistory() }
    }
}