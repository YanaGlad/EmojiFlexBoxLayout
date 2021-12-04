package com.example.emoji.fragments.channels.pager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emoji.model.StreamModel
import com.example.emoji.repository.StreamRepository
import com.example.emoji.viewState.StreamViewState
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.IOException

/**
 * @author y.gladkikh
 */
@ExperimentalSerializationApi
class StreamsViewModel @AssistedInject constructor(
    private val repo: StreamRepository,
) : ViewModel() {

    @AssistedFactory
    interface StreamsViewModelFactory {
        fun create(): StreamsViewModel
    }

    class Factory(
        val factory: StreamsViewModelFactory,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

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

        compositeDisposable.add(
            repo.getLocalStreams()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(TAG, "It LOCAL is ${(it as StreamViewState.Loaded).list}")
                        _viewState.postValue(it)
                    },
                    {
                        Log.d(TAG, "It LOCAL is ERROR ${it.message}")
                        _viewState.postValue(it.convertStreamToViewState())
                    }
                )
        )

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
                    }
                )
        )
    }
}
