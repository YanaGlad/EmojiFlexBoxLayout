package com.example.emoji.viewState.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class MessengerReducer : ScreenDslReducer<MessageEvent, MessageEvent.UI, MessageEvent.Internal, MessengerState,
        MessageEffect, MessengerCommand>(MessageEvent.UI::class, MessageEvent.Internal::class) {

    override fun Result.internal(event: MessageEvent.Internal): Any {
        return when (event) {
            is MessageEvent.Internal.ErrorLoading -> {
                state { copy(error = event.error, isLoading = false, isEmptyState = false) }
                effects { MessageEffect.HideKeyboard }
            }
            is MessageEvent.Internal.PageLoaded -> {
                state { copy(items = event.list, isLoading = false, isEmptyState = false) }
            }
            is MessageEvent.Internal.PageLoading -> {
                state { copy(
                    isLoading = true,
                    isEmptyState = false,
                    streamName = event.streamName,
                    topicName = event.topicName,
                    lastMessageId = event.lastMessageId,
                    count = event.count
                ) }
                commands { +MessengerCommand.MessagesLoaded(
                    streamName = state.streamName,
                    topicName = state.topicName,
                    lastMessageId = state.lastMessageId,
                    count = state.count
                ) }
            }
            is MessageEvent.Internal.AddMessage ->{
                state { copy(
                    isLoading = true,
                    isEmptyState = false,
                    streamName = event.streamName,
                    topicName = event.topicName,
                    lastMessageId = event.lastMessageId,
                    count = event.count,
                    text = event.text,
                    error = event.error
                ) }
                commands { +MessengerCommand.MessagesAdd(
                    streamName = state.streamName,
                    topicName = state.topicName,
                    error = state.error,
                    text = state.text
                ) }
            }
            is MessageEvent.Internal.SuccessOperation -> {
                state { copy(
                    isLoading = false,
                    isEmptyState = false,
                 ) }
            }
        }
    }

    override fun Result.ui(event: MessageEvent.UI): Any {
        return MessageEvent.UI.Stub
    }

}