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
    fun bindMessageRemoteDataProvider(impl: RemoteMessageDataProviderImpl)
            : RemoteMessageDataProvider

    @Binds
    @Singleton
    fun bindStreamRemoteDataProvider(impl: RemoteStreamDataProviderImpl)
            : RemoteStreamDataProvider

    @Binds
    @Singleton
    fun bindUsersRemoteDataProvider(impl: RemoteUserDataProviderImpl)
            : RemoteUserDataProvider

    @Binds
    @Singleton
    fun bindReactionsRemoteDataProvider(impl: RemoteReactionDataProviderImpl)
            : RemoteReactionDataProvider
}
