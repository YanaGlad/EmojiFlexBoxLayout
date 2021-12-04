package com.example.emoji.viewState.elm.messanger

import com.example.emoji.viewState.elm.laod.LoadMessages
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

/**
 * @author y.gladkikh
 */
class MessengerActor constructor(
    private val loadMessages: LoadMessages,
) : ActorCompat<MessengerCommand, MessageEvent.Internal> {

    override fun execute(command: MessengerCommand): Observable<MessageEvent.Internal> {
        when (command) {
            is MessengerCommand.LoadMessages -> {
                return loadMessages.getMessages(command.streamName, command.topicName, command.lastMessageId, command.count)
                    .mapEvents(
                        { list -> MessageEvent.Internal.PageLoaded(list) },
                        { error -> MessageEvent.Internal.ErrorLoading(error) }
                    )
            }

            is MessengerCommand.AddMessages -> {
                return loadMessages.addMessage(command.streamName, command.topicName, command.text)
                    .mapEvents(
                        successEvent = MessageEvent.Internal.SuccessOperation(),
                        failureEvent = MessageEvent.Internal.ErrorLoading(command.error)
                    )
            }
            is MessengerCommand.AddReaction -> {
                return loadMessages.addReaction(command.messageId, command.emojiName)
                    .mapEvents(
                        { reaction -> MessageEvent.Internal.ReactionAdded(command.messageId, command.emojiName, command.error) },
                        { error -> MessageEvent.Internal.ErrorLoading(error) }
                    )
            }
            is MessengerCommand.RemoveReaction -> {
                return loadMessages.removeReaction(command.messageId, command.emojiName)
                    .mapEvents(
                        successEvent = MessageEvent.Internal.SuccessOperation(),
                        failureEvent = MessageEvent.Internal.ErrorLoading(command.error)
                    )
            }
        }
    }
}