package com.example.emoji.dataprovider

import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import io.reactivex.Single
import retrofit2.http.Path

interface RemoteStreamDataProvider {
    fun getAllStreams(): Single<List<StreamModel>>
    fun getTopicsByStreamId(@Path("stream_id") id: Int): Single<List<TopicModel>>
}
