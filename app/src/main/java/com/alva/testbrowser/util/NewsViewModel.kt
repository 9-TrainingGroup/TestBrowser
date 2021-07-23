package com.alva.testbrowser.util

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.alva.testbrowser.database.NewsItem
import com.alva.testbrowser.database.NewsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository: NewsRepository = NewsRepository

    private val _pagingDataTT: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()
    val pagingDataTT: LiveData<PagingData<NewsItem>> = _pagingDataTT

    private val _pagingDataJX: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()
    val pagingDataJX: LiveData<PagingData<NewsItem>> = _pagingDataJX

    private val _pagingDataYL: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()
    val pagingDataYL: LiveData<PagingData<NewsItem>> = _pagingDataYL

    private val _pagingDataYD: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()
    val pagingDataYD: LiveData<PagingData<NewsItem>> = _pagingDataYD

    fun getPagingData(content: String, type: String) {
        viewModelScope.launch {
            repository.getPagingData(type).cachedIn(viewModelScope).map { pagingData ->
                pagingData.filter {
                    it.title.contains(content)
                }
            }.collectLatest {
                when (type) {
                    "T1348647853363" -> _pagingDataTT.value = it
                    "T1467284926140" -> _pagingDataJX.value = it
                    "T1348648517839" -> _pagingDataYL.value = it
                    else -> _pagingDataYD.value = it
                }
            }
        }
    }
}