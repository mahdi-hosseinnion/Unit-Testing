package com.example.unittesting.Models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    private val id: Int,

    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "timeStamp")
    val timeStamp: String

) : Parcelable
