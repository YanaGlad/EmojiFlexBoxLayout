package com.example.emoji.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import androidx.room.Update
import com.example.emoji.db.entity.MessageDB
import io.reactivex.Flowable

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(item : MessageDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMessages(items : List<MessageDB>)

    @Query("Select * from `message_table`" )
    fun getAllMessages() : Flowable<List<MessageDB>>

    @Delete()
    fun deleteMessage(item : MessageDB)

    @Update
    fun updateMessage(item : MessageDB)
}
