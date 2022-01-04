package com.example.emoji.viewState.elm.laod

import com.example.emoji.api.model.Message
import com.example.emoji.api.model.Reaction
import com.example.emoji.repository.MessageRepository
import com.example.emoji.repository.ReactionRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class LoadMessages @Inject constructor(
    private val repoMessage: MessageRepository,
    private val repoReaction: ReactionRepository,
) {
    fun removeReaction(id: Int, reactionName: String): Completable {
        return repoReaction.removeReaction(id, reactionName)
    }

    fun addReaction(messageId: Int, name: String): Single<Reaction> {
        return repoReaction.addReaction(messageId, name)
    }

    fun getListOfReactions(): Single<List<Reaction>> {
        return repoReaction.getReactionsFromServer()
    }

    fun getMessages(
        streamName: String,
        topicName: String,
        lastMessageId: Int,
        count: Int,
    ): Single<List<Message>> {
        return repoMessage.getMessages(streamName, topicName, lastMessageId, count)
    }

    fun addMessage(
        streamName: String,
        topicName: String,
        text: String,
    ): Completable {
        return repoMessage.addMessage(
            streamName = streamName,
            topicName = topicName,
            text = text
        )
    }
}