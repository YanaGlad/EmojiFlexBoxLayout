package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ReactionRequest(
    @SerialName("emoji_name")
    val name: String,

    @SerialName("message_id")
    val messageId: Int,
)