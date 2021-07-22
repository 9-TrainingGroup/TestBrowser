package com.alva.testbrowser.test

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository: Repository = Repository

    val pagingDataTT: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()

    val pagingDataJX: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()

    val pagingDataYL: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()

    val pagingDataYD: MutableLiveData<PagingData<NewsItem>> = MutableLiveData()

    fun getPagingData(type: String) {
        viewModelScope.launch {
            repository.getPagingData(type).cachedIn(viewModelScope).collectLatest {
                when (type) {
                    "T1348647853363" -> pagingDataTT.value = it
                    "T1467284926140" -> pagingDataJX.value = it
                    "T1348648517839" -> pagingDataYL.value = it
                    else -> pagingDataYD.value = it
                }
            }
        }
    }

//    fun filter(content: String) {
//        pagingData = pagingData.map { pagingData ->
//            pagingData.filter {
//                it.title.contains(content)
//            }
//        } as StateFlow<PagingData<NewsItem>>
//    }
}