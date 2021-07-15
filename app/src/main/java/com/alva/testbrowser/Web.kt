package com.alva.testbrowser

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Bookmark(
    var name: String,
    var url: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable

@Parcelize
@Entity
data class History(
    var name: String,
    var url: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable