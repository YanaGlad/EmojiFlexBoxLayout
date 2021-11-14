package com.example.emoji.repository

import com.example.emoji.api.Api
import com.example.emoji.api.model.Message
import com.example.emoji.api.model.MessageResponse
import com.example.emoji.api.model.MessagesNarrowRequest
import com.example.emoji.api.model.Reaction
import com.example.emoji.dataprovider.RemoteMessageDataProvider
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ExperimentalSerializationApi
class MessageRepositoryImpl @Inject constructor(private val provider : RemoteMessageDataProvider) : MessageRepository {

    private fun getMessagesQuery(streamName: String, topicName: String, lastMessageId: Int, numBefore: Int): Map<String, String> {
        val narrow = mutableListOf(
            MessagesNarrowRequest("stream", streamName)
        )
        if (topicName.isNotEmpty()) narrow.add(MessagesNarrowRequest("topic", topicName))

        val anchor = if (lastMessageId > 0) lastMessageId.toString() else "newest"

        return mapOf(
            "anchor" to anchor,
            "num_before" to numBefore.toString(),
            "num_after" to "0",
            "narrow" to Json.encodeToString(narrow),
            "apply_markdown" to "false"
        )
    }

    private fun addMessageQuery(streamName: String, topicName: String, content: String): Map<String, String> {
        return mapOf(
            "type" to "stream",
            "to" to streamName,
            "content" to content,
            "topic" to topicName
        )
    }

    override fun getMessages(streamName: String, topicName: String, lastMessageId: Int, count: Int): Single<List<Message>> {
        val query = getMessagesQuery(streamName, topicName, lastMessageId, count)
        return provider.getMessages(query)
    }

    override fun addMessage(streamName: String, topicName: String, text: String): Completable {
        val query = addMessageQuery(streamName, topicName, text)
        return provider.sendMessage(query)
    }

    override fun addReaction(messageId: Int, name: String): Single<Reaction> {
        return provider.addMessageReaction(messageId, name)
    }

    override fun removeReaction(id: Int, reactionName: String): Completable {
        return provider.removeMessageReaction(id, reactionName)
    }
}