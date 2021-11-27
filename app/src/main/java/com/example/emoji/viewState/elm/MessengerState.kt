package com.example.emoji.viewState.elm

import android.os.Parcelable
import com.example.emoji.api.model.Message
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class MessengerState(
    val streamName: String = "general",
    val topicName: String = "test",
    val lastMessageId: Int = 0,
    val count: Int = 1500,
    val items: @RawValue List<Any> = emptyList(),
    val isEmptyState: Boolean = false,
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val pageNumber: Int = 0,
) : Parcelable

sealed class MessageEvent {
    sealed class UI : MessageEvent() {
        object LoadFirstPage : UI()
       // object LoadNextPage : UI()
    }

    sealed class Internal : MessageEvent() {
        class PageLoading(val streamName: String, val topicName: String, val lastMessageId: Int, val count: Int) : Internal()
        data class PageLoaded(val list: List<Message>) : Internal()
        data class ErrorLoading(val error: Throwable) : Internal()
    }
}

sealed class Effect {
    data class NextPageLoadError(val erorr: Throwable) : Effect()
}

sealed class MessengerCommand {
    class MessagesLoaded(val streamName: String, val topicName: String, val lastMessageId: Int, val count: Int) : MessengerCommand()

}

sealed class MessageEffect {
    object ShowNetworkError : MessageEffect()
    object ShowEnexpectedError : MessageEffect()
    object HideKeyboard : MessageEffect()
}