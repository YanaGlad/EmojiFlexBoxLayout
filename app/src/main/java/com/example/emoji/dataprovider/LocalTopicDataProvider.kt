package com.example.emoji.dataprovider

import com.example.emoji.model.TopicModel
import io.reactivex.Flowable

interface LocalTopicDataProvider {
    fun insertTopic(item : TopicModel, streamId : Int)
    fun insertAllTopics(items : List<TopicModel>)
    fun getAllTopics() : Flowable<List<TopicModel>>
    fun deleteTopic(item : TopicModel)
    fun updateTopic(item : TopicModel)
}
