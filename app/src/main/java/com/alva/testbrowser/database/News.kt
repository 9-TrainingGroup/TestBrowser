package com.alva.testbrowser.database

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class News(
    @SerializedName("T1348647853363") val itemTT: List<NewsItem>,
    @SerializedName("T1467284926140") val itemJX: List<NewsItem>,
    @SerializedName("T1348648517839") val itemYL: List<NewsItem>,
    @SerializedName("T1348649079062") val itemYD: List<NewsItem>
) : Parcelable

@Parcelize
@Serializable
data class NewsItem(
    @SerializedName("postid") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("imgsrc") val img: String,
    @SerializedName("source") val author: String,
    @SerializedName("lmodify") val time: String,
    @SerializedName("url") val url: String
) : Parcelable