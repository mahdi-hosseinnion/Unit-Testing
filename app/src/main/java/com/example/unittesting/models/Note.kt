package com.example.unittesting.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "content")
    var content: String?,
    @ColumnInfo(name = "timeStamp")
    var timeStamp: String

) : Parcelable
