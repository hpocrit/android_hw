package ru.kpfu.itis.android_hw.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "deleted")
data class DeletedUserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val deleteDate: Long
)
