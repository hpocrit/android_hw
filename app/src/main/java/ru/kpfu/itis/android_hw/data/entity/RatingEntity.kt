package ru.kpfu.itis.android_hw.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rating")
data class RatingEntity (
    @PrimaryKey val id: String,
    val userId: String,
    val seriesId: String,
    val rating: Float
)