package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class
Reaction(

    @SerialName("emoji_code")
    val code: String,

    @SerialName("emoji_name")
    val name: String,

    @SerialName("user_id")
    val userId: Int
)
