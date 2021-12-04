package com.example.emoji.di

import android.content.Context
import androidx.annotation.NonNull
import com.example.emoji.repository.MessageRepository
import com.example.emoji.repository.StreamRepository
import com.example.emoji.viewState.elm.messanger.MessengerGlobalDI
import com.example.emoji.viewState.elm.stream.StreamGlobalDI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author y.gladkikh
 */
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
    fun provideGlobalDI(repository: MessageRepository) : MessengerGlobalDI {
        return MessengerGlobalDI(repository)
    }

    @Singleton
    @Provides
    @NonNull
    fun provideStreamGlobalDI(repository: StreamRepository) : StreamGlobalDI {
        return StreamGlobalDI(repository)
    }
}
