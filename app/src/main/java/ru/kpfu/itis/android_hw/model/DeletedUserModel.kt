package ru.kpfu.itis.android_hw.model

import java.util.Date

data class DeletedUserModel (
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val deleteDate: Long
)