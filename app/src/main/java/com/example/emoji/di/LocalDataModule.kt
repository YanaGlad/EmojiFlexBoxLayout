package com.example.emoji.di

import com.example.emoji.dataprovider.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * @author y.gladkikh
 */
@Module
interface LocalDataModule {

    @Binds
    @Singleton
    fun bindMessageLocalDataProvider(messageDataProvider: LocalMessageDataProviderImpl): LocalMessageDataProvider

    @Binds
    @Singleton
    fun bindStreamLocalDataProvider(streamDataProvider: LocalStreamDataProviderImpl): LocalStreamDataProvider

    @Binds
    @Singleton
    fun bindTopicsLocalDataProvider(topicDataProvider: LocalTopicDataProviderImpl): LocalTopicDataProvider

    @Binds
    @Singleton
    fun bindUsersLocalDataProvider(usersLocalUserDataProvider: LocalUserDataProviderImpl): LocalUserDataProvider
}
