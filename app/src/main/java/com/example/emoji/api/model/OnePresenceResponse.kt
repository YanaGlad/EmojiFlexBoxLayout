package com.example.emoji.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class Presence(
    val userId: Int,
    val time: Long,
    val status: String,
) : Parcelable

@Serializable
data class OnePresenceResponse(
    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("status")
    val status: String,
)

@Serializable
data class TitlePresence(
    @SerialName("website")
    val oneResponse: OnePresenceResponse,
)


@Serializable
data class PresenceResponse(
    @SerialName("presence")
    val presence: TitlePresence,
)
