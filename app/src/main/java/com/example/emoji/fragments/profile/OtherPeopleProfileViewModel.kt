package com.example.emoji.fragments.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emoji.repository.UserRepositoryImpl
import com.example.emoji.viewState.PresenceViewState
import com.example.emoji.viewState.UserViewState
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

@ExperimentalSerializationApi
class OtherPeopleProfileViewModel @AssistedInject constructor(
    val repo: UserRepositoryImpl,
) : ViewModel() {

    @AssistedFactory
    interface OtherPeopleProfileViewModelFactory {
        fun create(): OtherPeopleProfileViewModel
    }

    class Factory(
        val factory: OtherPeopleProfileViewModelFactory,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

    private companion object {
        const val TAG = "TAG_OTHER_PROFILE"
    }

    private val compositeDisposable = CompositeDisposable()

    private val _viewStatePresence: MutableLiveData<PresenceViewState> = MutableLiveData()
    val viewStatePresence: LiveData<PresenceViewState>
        get() = _viewStatePresence

    private fun Throwable.convertToViewState() =
        when (this) {
            is IOException -> UserViewState.Error.NetworkError
            else -> UserViewState.Error.UnexpectedError
        }

    fun getPresence(userId: Int) {
        compositeDisposable.add(repo.getUserPresence(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    Log.d(TAG, "It is $it")
                    _viewStatePresence.postValue(PresenceViewState.Loaded(it))
                },
                {
                    Log.d(TAG, "It is ERROR ${it.message}")
                    it.convertToViewState()
                }
            )
        )
    }
}
