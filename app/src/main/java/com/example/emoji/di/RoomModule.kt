package com.example.emoji.di

import android.content.Context
import androidx.room.Room
import com.example.emoji.db.MessengerDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

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

}