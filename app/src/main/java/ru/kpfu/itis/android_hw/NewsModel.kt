package ru.kpfu.itis.android_hw

import androidx.annotation.DrawableRes

sealed class NewsModel {
    class NewsButton : NewsModel()
    class NewsDate : NewsModel()

    data class News(
        val newsId: Int,
        val newsTitle: String,
        val newsDetails: String? = null,
        @DrawableRes val newsImage: Int? = null,
        val isLiked: Boolean = false,
    ): NewsModel()
}
