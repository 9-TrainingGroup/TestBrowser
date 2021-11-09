package com.alva.testbrowser.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class News(
    @SerialName("T1348647853363") val itemTT: List<NewsItem> = emptyList(),
    @SerialName("T1467284926140") val itemJX: List<NewsItem> = emptyList(),
    @SerialName("T1348648517839") val itemYL: List<NewsItem> = emptyList(),
    @SerialName("T1348649079062") val itemYD: List<NewsItem> = emptyList()
) : Parcelable

@Parcelize
@Serializable
data class NewsItem(
    @SerialName("postid") val id: String = "",
    @SerialName("title") val title: String = "",
    @SerialName("imgsrc") val img: String = "",
    @SerialName("source") val author: String = "",
    @SerialName("lmodify") val time: String = "",
    @SerialName("url") val url: String = ""
) : Parcelable