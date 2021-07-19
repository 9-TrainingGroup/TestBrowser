package com.alva.testbrowser.test

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

object Repository {
    private const val PAGE_SIZE = 10

    private val newsService = NewsService.create()

    fun getPagingData(): Flow<PagingData<NewsItem>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE)
        ) {
            NewsPagingSource(newsService)
        }.flow
    }
}