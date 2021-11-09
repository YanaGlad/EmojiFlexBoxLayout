package com.example.emoji.fragments.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emoji.api.Api
import com.example.emoji.api.Instance
import com.example.emoji.repository.UserRepository
import com.example.emoji.viewState.PeopleViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

@ExperimentalSerializationApi
class PeopleViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _viewState: MutableLiveData<PeopleViewState> = MutableLiveData()
    val viewState: LiveData<PeopleViewState>
        get() = _viewState

    private companion object{
        const val TAG = "TAG_PEOPLE"
    }

    private fun Throwable.convertToViewState() =
        when (this) {
            is IOException -> PeopleViewState.Error.NetworkError
            else -> PeopleViewState.Error.UnexpectedError
        }

    fun dispose(){
        compositeDisposable.dispose()
    }

    fun loadUsers() {
        _viewState.value = PeopleViewState.Loading

        val api = Instance.getInstance().create(Api::class.java)
        val repo = UserRepository(api)

        compositeDisposable.add(repo.getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "It is $it")
                    _viewState.postValue(PeopleViewState.Loaded(it))
                },
                {
                    Log.d(TAG, "It is ERROR ${it.message}")
                    it.convertToViewState()
                }
            ))
    }
}