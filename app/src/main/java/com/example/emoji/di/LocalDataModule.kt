package com.example.emoji.di

import com.example.emoji.dataprovider.LocalMessageDataProvider
import com.example.emoji.dataprovider.LocalMessageDataProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface LocalDataModule {

    @Binds
    @Singleton
    fun bindMessageLocalDataProvider(messageDataProvider: LocalMessageDataProviderImpl): LocalMessageDataProvider

}