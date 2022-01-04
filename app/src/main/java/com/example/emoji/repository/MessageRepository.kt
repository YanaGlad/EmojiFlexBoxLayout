package com.example.emoji.repository

import com.example.emoji.api.model.Message
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author y.gladkikh
 */
interface MessageRepository {
    fun getMessages(streamName: String, topicName: String, lastMessageId: Int, count: Int): Single<List<Message>>
    fun getLocalMessages(): Single<List<Message>>
    fun addMessage(streamName: String, topicName: String, text: String): Completable
}
