package ru.kpfu.itis.android_hw.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android_hw.data.entity.LikeEntity

@Dao
interface LikeDao {
    @Query("select * from likes where userId = :userId and seriesId = :seriesId")
    fun getLikeById(userId : String, seriesId: String) : LikeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLikes(like: LikeEntity)

    @Query("delete from likes where userId = :userId and seriesId = :seriesId")
    fun deleteLikeById(userId : String, seriesId: String)

    @Query("delete from likes where seriesId = :seriesId")
    fun deleteLikeBySeriesId(seriesId: String)

    @Query("select seriesId from likes where userId = :userId")
    fun getLikeByUserId(userId : String) : List<String>?

}