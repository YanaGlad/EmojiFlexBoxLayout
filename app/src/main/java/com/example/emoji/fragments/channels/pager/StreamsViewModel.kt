package com.example.emoji.fragments.channels.pager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.emoji.api.Api
import com.example.emoji.api.Instance
import com.example.emoji.model.StreamModel
import com.example.emoji.repository.StreamRepository
import com.example.emoji.viewState.StreamViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

@ExperimentalSerializationApi
class StreamsViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _viewState: MutableLiveData<StreamViewState> = MutableLiveData()
    val viewState: LiveData<StreamViewState>
        get() = _viewState

    private companion object {
        const val TAG = "TAG_STREAM"
    }

    private fun Throwable.convertStreamToViewState() =
        when (this) {
            is IOException -> StreamViewState.Error.NetworkError
            else -> StreamViewState.Error.UnexpectedError
        }

    fun dispose() {
        compositeDisposable.dispose()
    }

    fun loadStreams() {
        val listModels: ArrayList<StreamModel> = arrayListOf()
        _viewState.value = StreamViewState.Loading

        val api = Instance.getInstance().create(Api::class.java)
        val repo = StreamRepository(api)

        compositeDisposable.add(
            repo.getStreams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    listModels.add(it)
                }
                .subscribe(
                    {
                        Log.d(TAG, "It is $it")
                        _viewState.postValue(StreamViewState.Loaded(listModels))
                    },
                    {
                        Log.d(TAG, "It is ERROR ${it.message}")
                        it.convertStreamToViewState()
                    }
                ))
    }

}