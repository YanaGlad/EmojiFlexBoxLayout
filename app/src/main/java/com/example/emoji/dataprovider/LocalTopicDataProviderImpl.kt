package com.example.emoji.dataprovider

import com.example.emoji.db.dao.TopicDao
import com.example.emoji.db.entity.TopicsDB
import com.example.emoji.model.TopicModel
import io.reactivex.Flowable
import javax.inject.Inject

class LocalTopicDataProviderImpl @Inject constructor(private val dao: TopicDao) : LocalTopicDataProvider {
    override fun insertTopic(item: TopicModel, streamId: Int) {
        dao.insertTopic(
            TopicsDB(
                title = item.title,
                maxMessageId = item.maxMessageId,
                streamId = streamId
            )
        )
    }

    override fun insertAllTopics(items: List<TopicModel>) {
        TODO("Not yet implemented")
    }

    override fun getAllTopics(): Flowable<List<TopicModel>> {
        return dao.getAllTopics().flatMap { it ->
            val list = it.map {
                TopicModel(
                    it.title,
                    it.maxMessageId
                )
            }
            Flowable.just(list)
        }
    }

    override fun deleteTopic(item: TopicModel) {
        TODO("Not yet implemented")
    }

    override fun updateTopic(item: TopicModel) {
        TODO("Not yet implemented")
    }
}
