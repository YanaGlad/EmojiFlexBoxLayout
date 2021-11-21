package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OneTopicResponse(

    @SerialName("name")
    val name: String,

    @SerialName("max_id")
    val maxId : Int
)
