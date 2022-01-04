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
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Singleton
    @Binds
    fun bindStreamsRepository(impl: StreamRepositoryImpl): StreamRepository

    @Singleton
    @Binds
    fun bindUsersRepository(impl: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    fun bindReactionRepository(impl: ReactionRepositoryImpl): ReactionRepository
}
