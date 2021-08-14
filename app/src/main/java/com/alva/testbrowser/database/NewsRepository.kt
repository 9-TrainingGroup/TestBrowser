package com.alva.testbrowser.database

import androidx.paging.*
import kotlinx.coroutines.flow.Flow

object NewsRepository {
    private val newsService = NewsService.create()

    fun getPagingData(type: String): Flow<PagingData<NewsItem>> {
        return Pager(
            PagingConfig(50)
        ) {
            NewsPagingSource(newsService, type)
        }.flow
    }
}