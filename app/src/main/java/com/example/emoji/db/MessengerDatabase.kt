package com.example.emoji.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.emoji.db.dao.MessageDao
import com.example.emoji.db.entity.*

@Database(entities = [MessageDB::class, ReactionsDB::class, StreamsDB::class, TopicsDB::class, UserDB::class], version = 1,
    exportSchema = false)
abstract class MessengerDatabase : RoomDatabase(){
    abstract fun messageDao() : MessageDao
}