package com.example.emoji.di

import com.example.emoji.repository.*
import dagger.Binds
import dagger.Module
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton

/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindMessageRepository(messageRepositoryImpl: MessageRepositoryImpl): MessageRepository

    @Singleton
    @Binds
    fun bindStreamsRepository(messageRepositoryImpl: StreamRepositoryImpl): StreamRepository

    @Singleton
    @Binds
    fun bindUsersRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}
