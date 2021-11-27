package com.example.emoji.viewState.elm.laod

import com.example.emoji.api.model.Message
import com.example.emoji.repository.MessageRepository
import io.reactivex.Single
import javax.inject.Inject

class LoadMessages @Inject constructor(
    private val repo: MessageRepository,
) {

    fun execute(
        streamName: String,
        topicName: String,
        lastMessageId: Int,
        count: Int,
    ): Single<List<Message>> {
        return repo.getMessages(streamName, topicName, lastMessageId, count)
    }

}