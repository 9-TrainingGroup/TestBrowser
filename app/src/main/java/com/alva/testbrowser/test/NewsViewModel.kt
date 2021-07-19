package com.alva.testbrowser.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class NewsViewModel : ViewModel() {
    fun getPagingData(): Flow<PagingData<NewsItem>> {
        return Repository.getPagingData().cachedIn(viewModelScope)
    }
}