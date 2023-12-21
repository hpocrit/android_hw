package ru.kpfu.itis.android_hw.data.migration

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.lang.Exception

class MIGRATION_1_2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        try {
            db.execSQL("alter table user add column address text not null default 'sample'")
        } catch (ex: Exception) {
            Log.e("DB_MIGRATION", "Error occurred: $ex")
        }
    }

}