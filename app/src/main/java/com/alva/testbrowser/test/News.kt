package com.alva.testbrowser.test

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    @SerializedName("T1348647853363") val item: List<NewsItem>
) : Parcelable

@Parcelize
data class NewsItem(
    @SerializedName("postid") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("imgsrc") val img: String,
    @SerializedName("source") val author: String,
    @SerializedName("lmodify") val time: String,
    @SerializedName("url") val url: String
) : Parcelable