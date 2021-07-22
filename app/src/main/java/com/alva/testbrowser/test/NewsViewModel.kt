package com.alva.testbrowser.test

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class NewsViewModel : ViewModel() {
    val initial: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also { it.value = false }

    val pagingData: Flow<PagingData<NewsItem>> =
        Repository.getPagingData("T1348647853363").cachedIn(viewModelScope)

    val jxData: Flow<PagingData<NewsItem>> =
        Repository.getPagingData("T1467284926140").cachedIn(viewModelScope)

    val ylData: Flow<PagingData<NewsItem>> =
        Repository.getPagingData("T1348648517839").cachedIn(viewModelScope)

    val ydData: Flow<PagingData<NewsItem>> =
        Repository.getPagingData("T1348649079062").cachedIn(viewModelScope)

//    fun filter(content: String) {
//        pagingData = Repository.filter(content)
//    }
}