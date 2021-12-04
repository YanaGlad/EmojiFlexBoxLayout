package com.example.emoji.db.dao

import androidx.room.*
import com.example.emoji.db.entity.UserDB
import io.reactivex.Flowable

/**
 * @author y.gladkikh
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(item: UserDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(items: List<UserDB>)

    @Query("Select * from `user_table`")
    fun getAllUsers(): Flowable<List<UserDB>>

    @Delete
    fun deleteUser(item: UserDB)

    @Update
    fun updateUser(item: UserDB)
}
