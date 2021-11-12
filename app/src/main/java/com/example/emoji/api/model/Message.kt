package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Message(
    val id: Int,
    val topicName: String,
    val authorId: Int,
    val authorName: String,
    val avatar_url: String,
    val content: String,
    val time: Long,
    val reactions: List<Reaction> = emptyList(),
    val is_me_message : Boolean
)

@Serializable
data class MessagesNarrowRequest(
    @SerialName("operator")
    val operator: String,

    @SerialName("operand")
    val operand: String
)


@Serializable
data class MessageResponse(

    @SerialName("id")
    val id: Int,

    @SerialName("msg")
    val msg: String,

    @SerialName("result")
    val result: String
)

@Serializable
data class MessagesResponse(

    @SerialName("messages")
    val oneMessages: List<OneMessageResponse>
)


@Serializable
data class OneMessageResponse(

    @SerialName("id")
    val id: Int,

    @SerialName("sender_id")
    val authorId: Int,

    @SerialName("sender_full_name")
    val authorName: String,

    @SerialName("avatar_url")
    val avatar_url: String,

    @SerialName("content")
    val content: String,

    @SerialName("timestamp")
    val time: Long,

    @SerialName("reactions")
    val reactions: List<Reaction>,

    @SerialName("subject")
    val topicName: String,

    @SerialName("is_me_message")
    val is_me_message : Boolean
)