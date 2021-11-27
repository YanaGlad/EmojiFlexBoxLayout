package com.example.emoji.di

import com.example.emoji.MainActivity
import com.example.emoji.fragments.channels.pager.StreamsViewModel
import com.example.emoji.fragments.message.MessageFragment
import com.example.emoji.fragments.message.MessageViewModel
import com.example.emoji.fragments.people.PeopleViewModel
import com.example.emoji.fragments.profile.OtherPeopleProfileViewModel
import com.example.emoji.fragments.profile.ProfileViewModel
import com.example.emoji.viewState.elm.GlobalDI
import dagger.Component
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Singleton


@ExperimentalSerializationApi
@Singleton
@Component(
    modules = [
    ApplicationModule::class,
    RetrofitModule::class,
    RepositoryModule::class,
    RemoteDataModule::class,
    LocalDataModule::class,
    RoomModule::class]
)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(globalDI: GlobalDI)
    fun inject(fragment: MessageFragment)

    fun messageViewModelAssistedFactory(): MessageViewModel.MessageViewModelAssistedFactory
    fun profileViewModelFactory(): ProfileViewModel.ProfileViewModelFactory
    fun otherPeopleProfileViewModelFactory(): OtherPeopleProfileViewModel.OtherPeopleProfileViewModelFactory
    fun streamsViewModelFactory(): StreamsViewModel.StreamsViewModelFactory
    fun usersViewModelFactory(): PeopleViewModel.PeopleViewModelAssistedFactory

}
