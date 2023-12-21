package ru.kpfu.itis.android_hw.model

sealed class Model {

    object FavoritesContainer : Model()
    data class SeriesModel(
        val id: String,
        val name: String,
        val year: Int,
        val image: String,
        val summary: String?
    ) : Model()

}