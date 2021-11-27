package com.example.emoji.di

import android.content.Context
import androidx.annotation.NonNull
import com.example.emoji.repository.MessageRepository
import com.example.emoji.viewState.elm.GlobalDI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(@param:NonNull private val context: Context) {

    @Singleton
    @Provides
    @NonNull
    fun provideContext(): Context {
        return context
    }

    @Singleton
    @Provides
    @NonNull
    fun provideGlobalDI(repository: MessageRepository) : GlobalDI{
        return GlobalDI(repository)
    }
}
