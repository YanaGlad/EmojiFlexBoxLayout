package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamsResponse(

    @SerialName("oneStreams")
    val oneStreams: List<OneStreamResponse>
)

@Serializable
data class OneStreamResponse(

    @SerialName("stream_id")
    val id: Int,

    @SerialName("name")
    val name: String
)
