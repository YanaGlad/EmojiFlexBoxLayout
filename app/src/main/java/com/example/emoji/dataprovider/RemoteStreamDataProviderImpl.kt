package com.example.emoji.dataprovider

import com.example.emoji.api.Api
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import io.reactivex.Single
import javax.inject.Inject

class RemoteStreamDataProviderImpl @Inject constructor(private val api: Api) : RemoteStreamDataProvider {
    override fun getAllStreams(): Single<List<StreamModel>> =
        api.getAllStreams()
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

    override fun getTopicsByStreamId(id: Int): Single<List<TopicModel>> =
        api.getTopicsByStreamId(id).flatMap {
            val mappedList = it.oneTopics.map {
                TopicModel(
                    title = it.name,
                    maxMessageId = it.maxId
                )
            }
            Single.just(mappedList)
        }
}
