package com.example.emoji.viewState

import com.example.emoji.api.model.User
import com.example.emoji.model.TopicModel

sealed class TopicViewState {
    object Loading : TopicViewState()

    class Loaded(val list: List<TopicModel>) : TopicViewState()

    class Error {
        object NetworkError : TopicViewState()
        object UnexpectedError : TopicViewState()
    }

    object SuccessOperation : TopicViewState()
}
