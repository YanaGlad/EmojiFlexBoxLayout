package com.example.emoji.di

import com.example.emoji.dataprovider.LocalUserDataProvider
import com.example.emoji.dataprovider.LocalMessageDataProviderImpl
import com.example.emoji.dataprovider.LocalMessageDataProvider
import com.example.emoji.dataprovider.LocalStreamDataProviderImpl
import com.example.emoji.dataprovider.LocalStreamDataProvider
import com.example.emoji.dataprovider.LocalTopicDataProvider
import com.example.emoji.dataprovider.LocalTopicDataProviderImpl
import com.example.emoji.dataprovider.LocalUserDataProviderImpl

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

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
