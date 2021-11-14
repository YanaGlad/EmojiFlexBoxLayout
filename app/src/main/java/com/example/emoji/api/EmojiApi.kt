package com.example.emoji.api

import com.example.emoji.api.model.MessagesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface EmojiApi {
    @GET("static/generated/emoji/emoji_codes.json")
    fun getMessages()

}