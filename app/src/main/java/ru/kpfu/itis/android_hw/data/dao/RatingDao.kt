package ru.kpfu.itis.android_hw.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android_hw.data.entity.LikeEntity
import ru.kpfu.itis.android_hw.data.entity.RatingEntity
import ru.kpfu.itis.android_hw.data.entity.UserEntity

@Dao
interface RatingDao {
    @Query("delete from rating where seriesId = :id")
    fun deleteRatingBySeriesId(id: String)

    @Query("delete from rating where id = :id")
    fun deleteRatingById(id: String)

    @Query("select * from rating where seriesId = :id")
    fun getAllRatingBySeriesId(id: String) : List<RatingEntity>?

    @Query("select * from rating where userId = :userId and seriesId = :seriesId")
    fun getRatingById(userId : String, seriesId: String) : RatingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE) // todo нужно сделать правильно
    fun addRating(rating: RatingEntity)

    @Query("update rating set rating = :rating where id = :id")
    fun updateRaring(id: String, rating: Float)
}