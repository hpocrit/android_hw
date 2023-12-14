package ru.kpfu.itis.android_hw.model

data class RatingModel (
    val id: String,
    val userId: String,
    val seriesId: String,
    val rating: Float
)