package com.alva.testbrowser.test

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsViewModel : ViewModel() {
    val initial: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also { it.value = false }

    var pagingData: Flow<PagingData<NewsItem>> = Repository.getPagingData().cachedIn(viewModelScope)

    fun filter() {
        pagingData.map { pagingData ->
            pagingData.filter {
                it.title.contains("1")
            }
        }
    }
}