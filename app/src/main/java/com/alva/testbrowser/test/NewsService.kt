package com.alva.testbrowser.test

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsService {
    @GET("nc/article/headline/{type}/{page}-100.html")
    suspend fun searchNews(@Path("page") page: Int, @Path("type") type: String): News

    companion object {
        private const val BASE_URL = "http://c.m.163.com/"

        fun create(): NewsService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsService::class.java)
        }
    }
}