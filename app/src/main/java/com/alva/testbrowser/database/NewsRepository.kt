package com.alva.testbrowser.database

import androidx.paging.*
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService
) {
    fun getPagingData(type: String) = Pager(
        PagingConfig(
            pageSize = 100,
            prefetchDistance = 1,
            initialLoadSize = 100
        )
    ) {
        NewsPagingSource(newsService, type)
    }.flow
}