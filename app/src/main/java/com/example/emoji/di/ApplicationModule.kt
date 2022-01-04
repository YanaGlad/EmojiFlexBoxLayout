package com.example.emoji.di

import android.content.Context
import androidx.annotation.NonNull
import com.example.emoji.repository.MessageRepository
import com.example.emoji.repository.ReactionRepository
import com.example.emoji.repository.StreamRepository
import com.example.emoji.repository.UserRepository
import com.example.emoji.viewState.elm.messanger.MessengerGlobalDI
import com.example.emoji.viewState.elm.stream.StreamGlobalDI
import com.example.emoji.viewState.elm.users.UserGlobalDI
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

    @Provides
    @NonNull
    fun provideGlobalDI(repositoryMessage: MessageRepository, repositoryReaction: ReactionRepository): MessengerGlobalDI {
        return MessengerGlobalDI(repositoryMessage, repositoryReaction)
    }

    @Provides
    @NonNull
    fun provideStreamGlobalDI(repository: StreamRepository): StreamGlobalDI {
        return StreamGlobalDI(repository)
    }

    @Provides
    @NonNull
    fun provideUserGlobalDI(repository: UserRepository): UserGlobalDI {
        return UserGlobalDI(repository)
    }
}
