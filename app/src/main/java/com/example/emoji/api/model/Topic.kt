package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TopicsResponse(

    @SerialName("topics")
    val oneTopics: List<OneTopicResponse>
)
