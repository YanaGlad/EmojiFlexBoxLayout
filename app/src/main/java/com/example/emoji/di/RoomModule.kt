package com.example.emoji.di

import android.content.Context
import androidx.room.Room
import com.example.emoji.db.MessengerDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author y.gladkikh
 */
@Module
class RoomModule {
    @Provides
    @Singleton
    fun provideMessengerDatabase(context : Context) : MessengerDatabase =
        Room.databaseBuilder(context, MessengerDatabase::class.java, "message-db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMessageDao(database: MessengerDatabase) = database.messageDao()

    @Provides
    @Singleton
    fun provideStreamDao(database: MessengerDatabase) = database.streamsDao()

    @Provides
    @Singleton
    fun provideTopicsDao(database: MessengerDatabase) = database.topicsDao()

    @Provides
    @Singleton
    fun provideUsersDao(database: MessengerDatabase) = database.usersDao()
}
