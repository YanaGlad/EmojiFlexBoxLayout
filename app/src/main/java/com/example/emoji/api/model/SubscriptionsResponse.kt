package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionsResponse(

    @SerialName("subscriptions")
    val subscriptions: List<OneStreamResponse>
)
