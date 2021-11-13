package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AllUsersResponse(

    @SerialName("members")
    val members: List<MyUserResponse>
)

data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val avatar_url: String,
    val is_active: Boolean,
)

@Serializable
data class MyUserResponse(

    @SerialName("user_id")
    val id: Int,

    @SerialName("full_name")
    val full_name: String,

    @SerialName("email")
    val email: String,

    @SerialName("avatar_url")
    val avatar_url: String,

    @SerialName("is_active")
    val is_active : Boolean
)
