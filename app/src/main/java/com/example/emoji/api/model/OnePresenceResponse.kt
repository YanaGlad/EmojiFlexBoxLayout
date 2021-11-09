package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Presence(
    val userId: Int,
    val time: Long
)

@Serializable
data class OnePresenceResponse(

    @SerialName("timestamp")
    val timestamp_sec: Long,

    @SerialName("status")
    val status: String
)

@Serializable
data class PresenceResponse(
    @SerialName("onePresence")
    val onePresence: Map<String, OnePresenceResponse> = emptyMap()
)