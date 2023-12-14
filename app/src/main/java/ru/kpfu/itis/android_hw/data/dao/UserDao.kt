package ru.kpfu.itis.android_hw.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.kpfu.itis.android_hw.data.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // todo нужно сделать правильно
    fun addUser(user: UserEntity)

    @Query("select * from user where id = :userId")
    fun getUserInfoById(userId : String) : UserEntity?

    @Query("select * from user")
    fun getAllUser() : List<UserEntity>?

    @Query("update user set email = :email where id = :id")
    fun updateUserAddress(id: String, email: String)

    @Query("update user set phone = :phone where id = :id")
    fun updateUserPhone(id: String, phone: String)

    @Query("update user set password = :password where id = :id")
    fun updateUserPassword(id: String, password: String)

    @Query("delete from user where id = :id")
    fun deleteUserById(id: String)
}