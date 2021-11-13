package com.example.emoji.repository

import com.example.emoji.api.Api
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import io.reactivex.Observable
import io.reactivex.Single

class StreamRepository(private val apiService: Api) {

    fun getStreams(): Observable<StreamModel> {
        return apiService.getAllStreams()
            .flatMap { it ->
                val mappedList = it.oneStreams.map {
                    StreamModel(
                        id = it.id,
                        title = it.name,
                        topics = listOf(),
                        subscribed = true,
                        clicked = false
                    )
                }
                Single.just(mappedList)
            }
            .toObservable()
            .flatMap { Observable.fromIterable(it) }
            .flatMap { stream ->
                apiService.getTopicsByStreamId(stream.id)
                    .map { it ->
                        val mappedTopics = it.oneTopics.map { TopicModel(title = it.name, maxMessageId = it.max_id) }
                        StreamModel(
                            id = stream.id,
                            title = stream.title,
                            topics = mappedTopics,
                            subscribed = true,
                            clicked = false
                        )
                    }.toObservable()
            }
    }
}