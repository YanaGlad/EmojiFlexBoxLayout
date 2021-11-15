package com.example.emoji.viewState

import com.example.emoji.api.model.Message

sealed class MessageViewState {
    object Loading : MessageViewState()

    class Loaded(val list: List<Message>) : MessageViewState()

    class Error {
        object NetworkError : MessageViewState()
        object UnexpectedError : MessageViewState()
    }

    object SuccessOperation : MessageViewState()
}
