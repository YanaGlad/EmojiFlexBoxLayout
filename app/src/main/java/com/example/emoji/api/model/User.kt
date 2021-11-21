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
    val fullName: String,
    val email: String,
    val avatarUrl: String,
    val isActive: Boolean,
)

@Serializable
data class MyUserResponse(

    @SerialName("user_id")
    val id: Int,

    @SerialName("full_name")
    val fullName: String,

    @SerialName("email")
    val email: String,

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("is_active")
    val isActive : Boolean
)
