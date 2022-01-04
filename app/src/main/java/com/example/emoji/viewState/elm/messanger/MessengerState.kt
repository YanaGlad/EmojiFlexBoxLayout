package com.example.emoji.viewState.elm.messanger

import android.os.Parcelable
import com.example.emoji.api.model.Message
import com.example.emoji.api.model.Reaction
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * @author y.gladkikh
 */
@Parcelize
data class MessengerState(
    val streamName: String = "general",
    val topicName: String = "test",
    val lastMessageId: Int = 0,
    val count: Int = 1500,
    val items: @RawValue List<Any> = emptyList(),
    val reactions: @RawValue List<Reaction> = emptyList(),
    val isReaction: Boolean = false,
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val pageNumber: Int = 0,
    val myUserId: Int = 0,
    val myUserName: String = "Yana Glad",
    val text: String = "",
    val messageId: Int = 0,
    val emojiName: String = "",
) : Parcelable

sealed class MessageEvent {
    sealed class UI : MessageEvent() {
        object Init : UI()
    }

    sealed class Internal : MessageEvent() {
        class StreamLoading(val streamName: String, val topicName: String, val lastMessageId: Int = 0, val count: Int = 1500) : Internal()
        data class PageLoaded(val list: List<Message>) : Internal()
        data class ErrorLoading(val error: Throwable?) : Internal()
        data class ReactionsList(val error: Throwable?, val list: List<Reaction>) : Internal()
        object ReactionsLoading : Internal()

        class MessageAdded(
            val streamName: String,
            val topicName: String,
            val text: String,
            val error: Throwable,
            val lastMessageId: Int = 0,
            val count: Int = 1500,
        ) : Internal()

        class ReactionAdded(val messageId: Int, val emojiName: String, val error: Throwable?) : Internal()
        class ReactionRemoved(val messageId: Int, val emojiName: String, val topicTitle: String, val streamTitle: String, val error: Throwable) : Internal()

        class SuccessOperation() : Internal()
    }
}

sealed class Effect {
    data class ShowErrorSnackBar(val erorr: Throwable) : Effect()
}

sealed class MessengerCommand {
    class LoadMessages(val streamName: String, val topicName: String, val lastMessageId: Int, val count: Int) : MessengerCommand()
    class AddMessages(val streamName: String, val topicName: String, val text: String, val error: Throwable?) : MessengerCommand()
    class AddReaction(val messageId: Int, val emojiName: String, val error: Throwable?) : MessengerCommand()
    class RemoveReaction(val messageId: Int, val emojiName: String, val topicTitle: String, val streamTitle: String, val error: Throwable?) : MessengerCommand()
    class LoadReactions(val error: Throwable?) : MessengerCommand()

}

sealed class MessageEffect {
    object ShowNetworkError : MessageEffect()
    object ShowEnexpectedError : MessageEffect()
    object HideKeyboard : MessageEffect()
    object UpdateMessageList : MessageEffect()
}