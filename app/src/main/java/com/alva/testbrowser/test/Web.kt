package com.alva.testbrowser.test

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Bookmarktest @JvmOverloads constructor(
    var name: String,
    var url: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable

@Parcelize
@Entity
data class Historytest @JvmOverloads constructor(
    var name: String,
    var url: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) : Parcelable