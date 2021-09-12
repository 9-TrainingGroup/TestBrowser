package com.alva.testbrowser.database

import retrofit2.http.GET
import retrofit2.http.Path

interface NewsService {
    @GET("nc/article/headline/{type}/{page}-100.html")
    suspend fun searchNews(@Path("page") page: Int, @Path("type") type: String): News
}