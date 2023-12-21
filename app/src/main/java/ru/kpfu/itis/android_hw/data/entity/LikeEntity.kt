package ru.kpfu.itis.android_hw.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class LikeEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val seriesId: String
)
