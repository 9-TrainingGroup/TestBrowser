package com.alva.testbrowser.test

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

object Repository {
    private val newsService = NewsService.create()

    fun getPagingData(type: String): Flow<PagingData<NewsItem>> {
        return Pager(
            PagingConfig(50)
        ) {
            NewsPagingSource(newsService, type)
        }.flow
    }
}