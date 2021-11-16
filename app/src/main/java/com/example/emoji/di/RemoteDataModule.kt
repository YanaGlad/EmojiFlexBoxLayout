package com.example.emoji.di

import com.example.emoji.dataprovider.RemoteMessageDataProvider
import com.example.emoji.dataprovider.RemoteMessageDataProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RemoteDataModule {
    @Binds
    @Singleton
    fun bindMessageRemoteDataProvider(remoteMessageDataProvider: RemoteMessageDataProviderImpl): RemoteMessageDataProvider
}