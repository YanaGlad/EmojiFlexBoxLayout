package com.example.emoji.dataprovider

import com.example.emoji.db.dao.StreamDao
import com.example.emoji.db.dao.TopicDao
import com.example.emoji.db.entity.StreamsDB
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import io.reactivex.Single
import javax.inject.Inject

class LocalStreamDataProviderImpl @Inject constructor(
    private val streamDao: StreamDao,
    private val topicDao: TopicDao,
) : LocalStreamDataProvider {

    override fun insertStream(item: StreamModel) {
        streamDao.insertStream(
            StreamsDB(
                id = item.id,
                subscribed = item.subscribed,
                title = item.title,
                clicked = item.clicked
            )
        )
    }

    override fun insertAllStreams(items: List<StreamModel>) {
        streamDao.insertAllStreams(
            items.map {
                StreamsDB(
                    id = it.id,
                    subscribed = it.subscribed,
                    title = it.title,
                    clicked = it.clicked
                )
            }
        )
    }

    override fun getAllStreams(): Single<List<StreamModel>> {
        return streamDao.getAllStreams().flatMap {
            val mappedList = it.map {
                StreamModel(
                    id = it.id,
                    subscribed = it.subscribed,
                    title = it.title,
                    clicked = it.clicked,
                    topics = listOf()
                )
            }
            Single.just(mappedList)
        }
    }

    override fun deleteStream(item: StreamModel) {
        streamDao.deleteStream(
            StreamsDB(
                id = item.id,
                subscribed = item.subscribed,
                title = item.title,
                clicked = item.clicked
            )
        )
    }

    override fun updateStream(item: StreamModel) {
        streamDao.updateStream(
            StreamsDB(
                id = item.id,
                subscribed = item.subscribed,
                title = item.title,
                clicked = item.clicked
            )
        )
    }

    override fun getTopicsByStreamId(streamId: Int): Single<List<TopicModel>> {
        return topicDao.getTopicsByStreamId(streamId).flatMap { topicResponse ->
            Single.just(topicResponse.map {
                TopicModel(
                    it.title,
                    it.maxMessageId
                )
            })
        }
    }
}
