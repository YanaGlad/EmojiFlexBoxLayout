package com.example.emoji.viewState

import com.example.emoji.api.model.Message

/**
 * @author y.gladkikh
 */
sealed class MessageViewState {
    object Loading : MessageViewState()

    class Loaded(val list: List<Message>) : MessageViewState()

    class Error {
        object NetworkError : MessageViewState()
        object UnexpectedError : MessageViewState()
    }

    object SuccessOperation : MessageViewState()
}
