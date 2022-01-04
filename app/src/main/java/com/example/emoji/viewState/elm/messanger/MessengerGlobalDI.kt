package com.example.emoji.viewState.elm.messanger

import com.example.emoji.repository.MessageRepository
import com.example.emoji.repository.ReactionRepository
import com.example.emoji.viewState.elm.laod.LoadMessages
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class MessengerGlobalDI @Inject constructor(val messageRepository: MessageRepository, reactionRepository: ReactionRepository) {

    private val loadMessages by lazy { LoadMessages(messageRepository, reactionRepository) }

    val actor by lazy { MessengerActor(loadMessages) }
}