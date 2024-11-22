package com.dicoding.asclepius.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "saved_analyze")
@Parcelize
data class SavedAnalyze (
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Int = 0,

    @field:ColumnInfo(name = "image")
    var image: String = "",

    @field:ColumnInfo(name = "confidence_score")
    var confidence_score: String = "",

    ) : Parcelable