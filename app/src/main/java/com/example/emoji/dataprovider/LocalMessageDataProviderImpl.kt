package com.example.emoji.dataprovider

import com.example.emoji.api.model.Message
import com.example.emoji.db.dao.MessageDao
import com.example.emoji.db.entity.MessageDB
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
                avatarUrl = item.avatarUrl,
                content = item.content,
                time = item.time,
                isMeMessage = item.isMeMessage
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
                    avatarUrl = it.avatarUrl,
                    content = it.content,
                    time = it.time,
                    isMeMessage = it.isMeMessage
                )
            }
        )
    }

    override fun getAllMessages(): Flowable<List<Message>> {
        return dao.getAllMessages()
            .map { it ->
                it.map {
                    Message(
                        id = it.id,
                        topicName = it.topicName,
                        authorId = it.authorId,
                        authorName = it.authorName,
                        avatarUrl = it.avatarUrl,
                        content = it.content,
                        time = it.time,
                        reactions = listOf(),
                        isMeMessage = it.isMeMessage
                    )
                }
            }
    }

    override fun deleteMessage(item: Message) {
        TODO("Not yet implemented")
    }

    override fun updateMessage(item: Message) {
        TODO("Not yet implemented")
    }
}
