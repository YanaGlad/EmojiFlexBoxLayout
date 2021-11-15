package com.example.emoji.di

import com.example.emoji.repository.MessageRepository
import com.example.emoji.repository.MessageRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindMessageRepository(messageRepositoryImpl: MessageRepositoryImpl): MessageRepository

}