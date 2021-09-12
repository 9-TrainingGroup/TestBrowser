package com.alva.testbrowser.test

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Bookmarks @JvmOverloads constructor( //Java中调用kotlin的方法
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String
) : Parcelable

@Parcelize
@Entity
data class Histories @JvmOverloads constructor( //Java中调用kotlin的方法
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String
) : Parcelable