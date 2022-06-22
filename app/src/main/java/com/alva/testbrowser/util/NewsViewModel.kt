package com.alva.testbrowser.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.alva.testbrowser.database.NewsItem
import com.alva.testbrowser.database.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _pagingDataTT: MutableStateFlow<PagingData<NewsItem>>
            by lazy { MutableStateFlow(PagingData.empty()) }
    val pagingDataTT: StateFlow<PagingData<NewsItem>> get() = _pagingDataTT

    private val _pagingDataJX: MutableStateFlow<PagingData<NewsItem>>
            by lazy { MutableStateFlow(PagingData.empty()) }
    val pagingDataJX: StateFlow<PagingData<NewsItem>> get() = _pagingDataJX

    private val _pagingDataYL: MutableStateFlow<PagingData<NewsItem>>
            by lazy { MutableStateFlow(PagingData.empty()) }
    val pagingDataYL: StateFlow<PagingData<NewsItem>> get() = _pagingDataYL

    private val _pagingDataYD: MutableStateFlow<PagingData<NewsItem>>
            by lazy { MutableStateFlow(PagingData.empty()) }
    val pagingDataYD: StateFlow<PagingData<NewsItem>> get() = _pagingDataYD

    fun getPagingData(content: String, type: String) =
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.getPagingData(type).cachedIn(viewModelScope).map { pagingData ->
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