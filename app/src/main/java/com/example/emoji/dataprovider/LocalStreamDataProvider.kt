package com.example.emoji.dataprovider

import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import io.reactivex.Single

interface LocalStreamDataProvider {
    fun insertStream(item: StreamModel)
    fun insertAllStreams(items: List<StreamModel>)
    fun getAllStreams(): Single<List<StreamModel>>
    fun deleteStream(item: StreamModel)
    fun updateStream(item: StreamModel)
    fun getTopicsByStreamId(streamId: Int): Single<List<TopicModel>>
}

