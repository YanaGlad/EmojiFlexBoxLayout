package com.example.emoji.dataprovider

import com.example.emoji.api.model.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface RemoteMessageDataProvider {
    fun getMessages(queryMap: Map<String, String>): Single<List<Message>>
    fun sendMessage(queryMap: Map<String, String>): Completable
    fun addMessageReaction(id: Int, reactionName: String): Single<Reaction>
    fun removeMessageReaction(id: Int, reactionName: String): Completable
}