package com.alva.testbrowser.test

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.lang.Exception

class NewsPagingSource(private val newsService: NewsService) : PagingSource<Int, NewsItem>() {
    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        return try {
            val page = params.key ?: 1
            val news = newsService.searchNews((0..4).shuffled()[0] * 100)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (news.item.isNotEmpty()) page + 1 else null
            LoadResult.Page(news.item, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}