package ru.kpfu.itis.android_hw.data.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kpfu.itis.android_hw.data.InceptionDatabase
import ru.kpfu.itis.android_hw.data.migration.MIGRATION_1_2
import java.lang.IllegalStateException

object ServiceLocator {
    private var dbInstance: InceptionDatabase? = null
    private var inceptionPref: SharedPreferences? = null
    fun initDatabase(ctx: Context) {
        dbInstance =  Room.databaseBuilder(ctx, InceptionDatabase::class.java, "inception.db").build()
//            .addMigrations(
//                MIGRATION_1_2()
//            )
        inceptionPref = ctx.getSharedPreferences("inception_pref", Context.MODE_PRIVATE)
    }
    fun getDbInstance(): InceptionDatabase {
        return dbInstance?: throw IllegalStateException("Db not initialized")
    }

    fun getSharedPref(): SharedPreferences {
        return inceptionPref?: throw IllegalStateException("Preferences not initialized")
    }
}