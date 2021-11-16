package com.example.emoji.repository

import com.example.emoji.api.model.Message
import com.example.emoji.api.model.MessageResponse
import com.example.emoji.api.model.Reaction
import com.example.emoji.viewState.MessageViewState
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {
    fun getMessages(streamName: String, topicName: String, lastMessageId: Int, count: Int): Single<MessageViewState>
    fun getLocalMessages( ): Single<MessageViewState>
    fun addMessage(streamName: String, topicName: String, text: String): Completable
    fun addReaction(messageId: Int, name: String): Single<Reaction>
    fun removeReaction(id: Int, reactionName: String): Completable
}