package com.example.emoji.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.emoji.db.dao.MessageDao
import com.example.emoji.db.dao.StreamDao
import com.example.emoji.db.dao.TopicDao
import com.example.emoji.db.dao.UserDao
import com.example.emoji.db.entity.*

/**
 * @author y.gladkikh
 */
@Database(entities = [
    MessageDB::class,
    ReactionsDB::class,
    StreamsDB::class,
    TopicsDB::class,
    UserDB::class
],
    version = 6,
    exportSchema = false)
abstract class MessengerDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun streamsDao(): StreamDao
    abstract fun topicsDao(): TopicDao
    abstract fun usersDao(): UserDao
}
