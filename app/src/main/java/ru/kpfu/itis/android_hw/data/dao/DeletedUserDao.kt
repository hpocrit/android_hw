package ru.kpfu.itis.android_hw.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android_hw.data.entity.DeletedUserEntity
import ru.kpfu.itis.android_hw.data.entity.UserEntity

@Dao
interface DeletedUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: DeletedUserEntity)

    @Query("DELETE FROM deleted WHERE deleteDate < :time")
    fun deleteOldItems(time: Long)

    @Query("select * from deleted")
    fun getAllUser() : List<DeletedUserEntity>?

    @Query("delete from deleted where id = :id")
    fun deleteUserById(id: String)

}