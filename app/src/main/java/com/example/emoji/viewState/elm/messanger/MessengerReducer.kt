package com.example.emoji.viewState.elm.messanger

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

/**
 * @author y.gladkikh
 */
class MessengerReducer : ScreenDslReducer<MessageEvent, MessageEvent.UI, MessageEvent.Internal, MessengerState,
        MessageEffect, MessengerCommand>(MessageEvent.UI::class, MessageEvent.Internal::class) {

    override fun Result.internal(event: MessageEvent.Internal): Any {
        return when (event) {
            is MessageEvent.Internal.ErrorLoading -> {
                state { copy(error = event.error, isLoading = false) }
                effects { MessageEffect.HideKeyboard }
            }
            is MessageEvent.Internal.PageLoaded -> {
                state { copy(items = event.list, isLoading = false, isReaction = false) }
            }
            is MessageEvent.Internal.StreamLoading -> {
                state {
                    copy(
                        isLoading = true,
                        streamName = event.streamName,
                        topicName = event.topicName,
                        lastMessageId = event.lastMessageId,
                        count = event.count
                    )
                }
                commands {
                    +MessengerCommand.LoadMessages(
                        streamName = state.streamName,
                        topicName = state.topicName,
                        lastMessageId = state.lastMessageId,
                        count = state.count
                    )
                }
                effects { }
            }
            is MessageEvent.Internal.MessageAdded -> {
                state {
                    copy(
                        isLoading = true,
                        streamName = event.streamName,
                        topicName = event.topicName,
                        lastMessageId = event.lastMessageId,
                        count = event.count,
                        text = event.text,
                        error = event.error
                    )
                }
                commands {
                    +MessengerCommand.AddMessages(
                        streamName = state.streamName,
                        topicName = state.topicName,
                        error = state.error,
                        text = state.text
                    )
                }
            }
            is MessageEvent.Internal.SuccessOperation -> {
                state {
                    copy(
                        isLoading = false,
                    )
                }
            }
            is MessageEvent.Internal.ReactionAdded -> {
                state {
                    copy(
                        isLoading = false,
                        messageId = event.messageId,
                        emojiName = event.emojiName
                    )
                }
                commands {
                    +MessengerCommand.AddReaction(
                        messageId = event.messageId,
                        emojiName = event.emojiName,
                        error = event.error
                    )
                }
            }
            is MessageEvent.Internal.ReactionRemoved -> {
                state {
                    copy(
                        isLoading = false,
                        messageId = event.messageId,
                        emojiName = event.emojiName,
                        streamName = event.streamTitle,
                        topicName = event.topicTitle,
                    )
                }
                commands {
                    +MessengerCommand.RemoveReaction(
                        messageId = event.messageId,
                        emojiName = event.emojiName,
                        streamTitle = event.streamTitle,
                        topicTitle = event.topicTitle,
                        error = event.error
                    )
                }
                effects {
                    +MessageEffect.UpdateMessageList
                }
            }
            is MessageEvent.Internal.ReactionsList -> {
                state { copy(reactions = event.list, isReaction = true) }
            }
            MessageEvent.Internal.ReactionsLoading -> {
                commands {
                    +MessengerCommand.LoadReactions(Exception())
                }
            }
        }
    }

    override fun Result.ui(event: MessageEvent.UI): Any {
        return when (event) {
            is MessageEvent.UI.Init -> {
                commands {
                    +MessengerCommand.LoadMessages(state.streamName, state.topicName, state.lastMessageId, state.count)
                    +MessengerCommand.LoadReactions(Exception())
                }
            }
        }
    }

}