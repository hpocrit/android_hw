package ru.kpfu.itis.android_hw.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android_hw.data.entity.SeriesEntity
import ru.kpfu.itis.android_hw.data.entity.UserEntity

@Dao
interface SeriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSeries(series: SeriesEntity)

    @Query("select * from series where id = :id")
    fun getSeriesInfoById(id : String) : SeriesEntity?

    @Query("select * from series")
    fun getAllSeries() : List<SeriesEntity>?

    @Query("update series set year = :year where id = :id")
    fun updateSeriesAddress(id: String, year: String)

    @Query("delete from series where id = :id")
    fun deleteSeriesById(id: String)
}