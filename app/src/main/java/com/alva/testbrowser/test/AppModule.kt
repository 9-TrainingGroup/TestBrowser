package com.alva.testbrowser.test

import android.content.Context
import androidx.room.Room
import com.alva.testbrowser.database.NewsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
 * Created by 无聊r丶 on 2021/9/13.
 * Copyright (c) 2021 All rights reserved.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "http://c.m.163.com/"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, WebsDatabase::class.java, "bookmark_database").build()

    @Singleton
    @Provides
    fun provideNewsService(): NewsService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsService::class.java)
}