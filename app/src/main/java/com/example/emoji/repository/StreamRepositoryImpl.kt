package com.example.emoji.repository

import android.util.Log
import com.example.emoji.dataprovider.LocalStreamDataProvider
import com.example.emoji.dataprovider.LocalTopicDataProvider
import com.example.emoji.dataprovider.RemoteStreamDataProvider
import com.example.emoji.model.StreamModel
import com.example.emoji.viewState.StreamViewState
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class StreamRepositoryImpl @Inject constructor(
    private val remote: RemoteStreamDataProvider,
    private val localStreams: LocalStreamDataProvider,
    private val localTopics: LocalTopicDataProvider,
) : StreamRepository {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private fun Throwable.convertToViewState(): StreamViewState =
        when (this) {
            is IOException -> StreamViewState.Error.NetworkError
            else -> StreamViewState.Error.UnexpectedError
        }

    fun dispose() {
        compositeDisposable.dispose()
    }

    override fun getStreams(): Observable<StreamModel> {
        return remote.getAllStreams()
            .toObservable()
            .flatMap { Observable.fromIterable(it) }
            .flatMap { stream ->
                remote.getTopicsByStreamId(stream.id)
                    .map { topic ->
                        val model = StreamModel(
                            id = stream.id,
                            title = stream.title,
                            topics = topic,
                            subscribed = true,
                            clicked = false
                        )
                        topic.forEach {
                            Log.d(TAG, "Gott topic ${it.title} ")
                            localTopics.insertTopic(it, stream.id)
                        }

                        localStreams.insertStream(model)
                        model
                    }.toObservable()
            }
    }

    override fun getLocalStreams(): Single<StreamViewState> {
        return Single.create { emitter ->
            val list: ArrayList<StreamModel> = arrayListOf()
            compositeDisposable.add(
                localStreams.getAllStreams()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(
                        { model ->
                            model.forEach { stream->
                                val topics = localStreams.getTopicsByStreamId(stream.id)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io())
                                    .subscribe(
                                        {
                                            stream.topics = it
                                        },
                                        {
                                        }
                                    )
                             }
                             Log.d(TAG, "Local streams loaded $model ")
                            emitter.onSuccess(StreamViewState.Loaded(model))
                        },
                        {
                            Log.e(TAG, "Error in local $it")
                            emitter.onSuccess(it.convertToViewState())
                        }
                    )
            )
        }
    }

    companion object {
        private const val TAG = "streams_repository_tag"
    }
}
