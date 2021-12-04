package com.example.emoji.viewState.elm.laod

import com.example.emoji.api.model.Message
import com.example.emoji.api.model.Reaction
import com.example.emoji.repository.MessageRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class LoadMessages @Inject constructor(
    private val repo: MessageRepository,
) {
    fun removeReaction(id: Int, reactionName: String): Completable {
        return repo.removeReaction(id, reactionName)
    }

    fun addReaction(messageId: Int, name: String): Single<Reaction> {
        return repo.addReaction(messageId, name)
    }

    fun getMessages(
        streamName: String,
        topicName: String,
        lastMessageId: Int,
        count: Int,
    ): Single<List<Message>> {
        return repo.getMessages(streamName, topicName, lastMessageId, count)
    }

    fun addMessage(
        streamName: String,
        topicName: String,
        text: String,
    ): Completable {
        return repo.addMessage(
            streamName = streamName,
            topicName = topicName,
            text = text
        )
    }
}