package com.alva.testbrowser.test

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object Repository {
    private val newsService = NewsService.create()
    private var pagingData = Pager(PagingConfig(50)) { NewsPagingSource(newsService) }.flow

    fun getPagingData(): Flow<PagingData<NewsItem>> {
        return pagingData
    }

    fun filter(): Flow<PagingData<NewsItem>> {
        return pagingData.map { pagingData ->
            pagingData.filter {
                it.title.contains("媒体")
            }
        }
    }
}