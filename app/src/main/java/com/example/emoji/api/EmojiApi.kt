package com.example.emoji.api

import retrofit2.http.GET

interface EmojiApi {
    @GET("static/generated/emoji/emoji_codes.json")
    fun getMessages()

}
