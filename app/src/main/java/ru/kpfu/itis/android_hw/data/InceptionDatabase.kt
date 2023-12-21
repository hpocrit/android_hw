package ru.kpfu.itis.android_hw.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kpfu.itis.android_hw.data.converter.DateConverter
import ru.kpfu.itis.android_hw.data.dao.DeletedUserDao
import ru.kpfu.itis.android_hw.data.dao.LikeDao
import ru.kpfu.itis.android_hw.data.dao.RatingDao
import ru.kpfu.itis.android_hw.data.dao.SeriesDao
import ru.kpfu.itis.android_hw.data.dao.UserDao
import ru.kpfu.itis.android_hw.data.entity.DeletedUserEntity
import ru.kpfu.itis.android_hw.data.entity.LikeEntity
import ru.kpfu.itis.android_hw.data.entity.RatingEntity
import ru.kpfu.itis.android_hw.data.entity.SeriesEntity
import ru.kpfu.itis.android_hw.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, SeriesEntity::class, RatingEntity::class, LikeEntity::class, DeletedUserEntity::class],
    version = 1
)
@TypeConverters(DateConverter::class)

abstract class InceptionDatabase : RoomDatabase() {

    abstract val userDao : UserDao
    abstract val likeDao : LikeDao
    abstract val seriesDao : SeriesDao
    abstract val ratingDao : RatingDao
    abstract val deletedDao : DeletedUserDao

}