package com.alva.testbrowser.database

import androidx.paging.*
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService
) {
    fun getPagingData(type: String) = Pager(
        PagingConfig(50)
    ) {
        NewsPagingSource(newsService, type)
    }.flow
}