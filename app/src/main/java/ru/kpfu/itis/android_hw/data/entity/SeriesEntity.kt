package ru.kpfu.itis.android_hw.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class SeriesEntity (
    @PrimaryKey val id: String,
    val name: String,
    val year: Int,
    val image: String,
    val summary: String?
)