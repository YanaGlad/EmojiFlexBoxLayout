package com.example.emoji.viewState.elm.stream

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

/**
 * @author y.gladkikh
 */
class StreamReducer : ScreenDslReducer<StreamEvent, StreamEvent.UI, StreamEvent.Internal, StreamState,
        StreamEffect, StreamCommand>(StreamEvent.UI::class, StreamEvent.Internal::class) {

    override fun Result.internal(event: StreamEvent.Internal): Any {
        return when (event) {
            is StreamEvent.Internal.ErrorLoading -> {
                state { copy(error = event.error, isLoading = false) }
            }
            is StreamEvent.Internal.PageLoaded -> {
                state { copy(isLoading = false, model = event.model) }

            }
            is StreamEvent.Internal.PageLoading -> {
                state { copy(isLoading = true) }
                commands {
                    +StreamCommand.StreamsLoaded()
                }
            }
            is StreamEvent.Internal.SuccessOperation -> {
                state { copy(isLoading = false) }

            }
        }
    }

    override fun Result.ui(event: StreamEvent.UI): Any? {
        TODO("Not yet implemented")
    }
}