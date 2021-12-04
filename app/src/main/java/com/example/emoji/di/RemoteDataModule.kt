package com.example.emoji.di

import com.example.emoji.dataprovider.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * @author y.gladkikh
 */
@Module
interface RemoteDataModule {
    @Binds
    @Singleton
    fun bindMessageRemoteDataProvider(remoteMessageDataProvider: RemoteMessageDataProviderImpl)
            : RemoteMessageDataProvider

    @Binds
    @Singleton
    fun bindStreamRemoteDataProvider(remoteStreamDataProvider: RemoteStreamDataProviderImpl)
            : RemoteStreamDataProvider

    @Binds
    @Singleton
    fun bindUsersRemoteDataProvider(remoteUserDataProvider: RemoteUserDataProviderImpl)
            : RemoteUserDataProvider
}
