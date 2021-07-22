package com.alva.testbrowser.test

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object Repository {
    private val newsService = NewsService.create()

    fun getPagingData(type: String): Flow<PagingData<NewsItem>> {
        return Pager(
            PagingConfig(50)
        ) {
            NewsPagingSource(newsService, type)
        }.flow
    }

//    fun filter(content: String): Flow<PagingData<NewsItem>> {
//        return getPagingData("T1348647853363")
//        }
//    }
}