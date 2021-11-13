package com.example.emoji.repository

import com.example.emoji.api.Api
import com.example.emoji.api.model.Message
import com.example.emoji.api.model.MessageResponse
import com.example.emoji.api.model.MessagesNarrowRequest
import com.example.emoji.api.model.Reaction
import io.reactivex.Single
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
class MessageRepository(private val apiService: Api)  {

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
            "type" to  "stream",
            "to" to streamName,
            "content" to content,
            "topic" to topicName
        )
    }


    fun getMessages(streamName: String, topicName: String, lastMessageId: Int, count: Int): Single<List<Message>> {
        val query = getMessagesQuery(streamName, topicName, lastMessageId, count)

        return apiService.getMessages(query).flatMap { response ->
            Single.just(response.oneMessages)
        }
            .map { it ->
                it.map {
                    Message(
                        id = it.id,
                        topicName = it.topicName,
                        authorId = it.authorId,
                        authorName = it.authorName,
                        avatar_url = it.avatar_url,
                        content = it.content,
                        time  = it.time,
                        reactions = it.reactions,
                        is_me_message = it.is_me_message
                    )
                }
            }
    }

    fun addMessage(streamName: String, topicName: String, text: String): Single<MessageResponse> {
        val query = addMessageQuery(streamName, topicName, text)
        return apiService.sendMessage(query)
    }

    fun addReaction(messageId : Int, name : String) : Single<Reaction> {
      return apiService.addMessageReaction(messageId, name)
    }
}