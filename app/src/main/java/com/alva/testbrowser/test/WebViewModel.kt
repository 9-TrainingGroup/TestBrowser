package com.alva.testbrowser.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val websRepository: WebsRepository
) : ViewModel() {
    val allBookmark = websRepository.getAllBookmarks().asLiveData(Dispatchers.IO)

    val allHistory = websRepository.getAllHistories().asLiveData(Dispatchers.IO)

    fun insertWebs(webs: Bookmarks) =
        viewModelScope.launch(Dispatchers.IO) { websRepository.insertWebs(webs) }

    fun insertWebs(webs: Histories) =
        viewModelScope.launch(Dispatchers.IO) { websRepository.insertWebs(webs) }

    fun updateWebs(webs: Bookmarks) =
        viewModelScope.launch(Dispatchers.IO) { websRepository.updateWebs(webs) }

    fun deleteWebs(webs: Bookmarks) =
        viewModelScope.launch(Dispatchers.IO) { websRepository.deleteWebs(webs) }

    fun deleteWebs(webs: Histories) =
        viewModelScope.launch(Dispatchers.IO) { websRepository.deleteWebs(webs) }

    fun deleteAllBookmark() =
        viewModelScope.launch(Dispatchers.IO) { websRepository.deleteAllBookmark() }

    fun deleteAllHistory() =
        viewModelScope.launch(Dispatchers.IO) { websRepository.deleteAllHistory() }
}