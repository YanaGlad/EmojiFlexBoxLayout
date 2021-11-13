package com.example.emoji.fragments.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emoji.api.Api
import com.example.emoji.api.Instance
import com.example.emoji.repository.UserRepository
import com.example.emoji.viewState.PresenceViewState
import com.example.emoji.viewState.UserViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

@ExperimentalSerializationApi
class ProfileViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun dispose(){
        compositeDisposable.dispose()
    }

    private val _viewState: MutableLiveData<UserViewState> = MutableLiveData()
    val viewState: LiveData<UserViewState>
        get() = _viewState

    private val _viewStatePresence : MutableLiveData<PresenceViewState> = MutableLiveData()
    val viewStatePresence : LiveData<PresenceViewState>
        get() = _viewStatePresence

    private val userId : MutableLiveData<Int> = MutableLiveData()

    private companion object{
        const val TAG = "TAG_PROFILE"
    }

    private fun Throwable.convertToViewState() =
        when (this) {
            is IOException -> UserViewState.Error.NetworkError
            else -> UserViewState.Error.UnexpectedError
        }

    fun getMyUser(){
        _viewState.value = UserViewState.Loading

        val api = Instance.getInstance().create(Api::class.java)
        val repo = UserRepository(api)

        compositeDisposable.add(repo.getMyUser()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    Log.d(TAG, "It is $it")
                    userId.postValue(it.id)
                    _viewState.postValue(UserViewState.Loaded(it))
                },
                {
                    Log.d(TAG, "It is ERROR ${it.message}")
                    it.convertToViewState()
                }
            )
        )
    }

    fun getPresence(){
        val api = Instance.getInstance().create(Api::class.java)
        val repo = UserRepository(api)

        compositeDisposable.add(repo.getUserPresence(userId.value!!)
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