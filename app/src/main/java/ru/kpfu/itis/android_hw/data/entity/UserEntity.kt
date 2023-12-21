package ru.kpfu.itis.android_hw.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val password: String
)