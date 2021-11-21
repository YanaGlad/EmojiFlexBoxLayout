package com.example.emoji.dataprovider

import com.example.emoji.api.model.Message
import com.example.emoji.api.model.Reaction
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteMessageDataProvider {
    fun getMessages(queryMap: Map<String, String>): Single<List<Message>>
    fun sendMessage(queryMap: Map<String, String>): Completable
    fun addMessageReaction(id: Int, reactionName: String): Single<Reaction>
    fun removeMessageReaction(id: Int, reactionName: String): Completable
}
