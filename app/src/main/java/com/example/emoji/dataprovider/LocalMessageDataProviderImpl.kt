package com.example.emoji.dataprovider

import com.example.emoji.api.Api
import com.example.emoji.api.model.Message
import com.example.emoji.db.dao.MessageDao
import com.example.emoji.db.entity.MessageDB
import com.example.emoji.model.MessageModel
import io.reactivex.Flowable
import javax.inject.Inject

class LocalMessageDataProviderImpl @Inject constructor(private val dao: MessageDao)  : LocalMessageDataProvider {

    override fun insertMessage(item: Message) {
        dao.insertMessage(
            MessageDB(
                id = item.id,
                topicName = item.topicName,
                authorName = item.authorName,
                authorId = item.authorId,
                avatar_url = item.avatar_url,
                content = item.content,
                time = item.time,
                is_me_message = item.is_me_message
            )
        )
    }

    override fun insertAllMessages(items: List<Message>) {
        dao.insertAllMessages(
            items.map {
                MessageDB(
                    id = it.id,
                    topicName = it.topicName,
                    authorName = it.authorName,
                    authorId = it.authorId,
                    avatar_url = it.avatar_url,
                    content = it.content,
                    time = it.time,
                    is_me_message = it.is_me_message
                )
            }
        )
    }

    override fun getAllMessages(): Flowable<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(item: Message) {
        TODO("Not yet implemented")
    }

    override fun updateMessage(item: Message) {
        TODO("Not yet implemented")
    }
}