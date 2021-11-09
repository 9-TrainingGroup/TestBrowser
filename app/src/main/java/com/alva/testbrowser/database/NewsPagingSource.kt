package com.alva.testbrowser.database

import androidx.paging.*
import java.lang.Exception

class NewsPagingSource(private val newsService: NewsService, private val type: String) :
    PagingSource<Int, NewsItem>() {
    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> =
        try {
            val currentPage = params.key ?: 1
            val news = newsService.searchNews((0..4).shuffled()[0] * 100, type)
            val prevKey = if (currentPage > 1) currentPage - 1 else null
            val nextKey = currentPage + 1
            val newsItem = when (type) {
                "T1348647853363" -> news.itemTT
                "T1467284926140" -> news.itemJX
                "T1348648517839" -> news.itemYL
                else -> news.itemYD
            }
            LoadResult.Page(newsItem, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
}