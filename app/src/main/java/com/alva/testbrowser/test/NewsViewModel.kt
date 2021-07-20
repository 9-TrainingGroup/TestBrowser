package com.alva.testbrowser.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class NewsViewModel : ViewModel() {
    val initial: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also { it.value = false }

    val pagingData: Flow<PagingData<NewsItem>> = Repository.getPagingData().cachedIn(viewModelScope)
}