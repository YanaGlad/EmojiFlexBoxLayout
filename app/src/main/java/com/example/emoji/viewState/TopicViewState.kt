package com.example.emoji.viewState

import com.example.emoji.model.TopicModel

/**
 * @author y.gladkikh
 */
sealed class TopicViewState {
    object Loading : TopicViewState()

    class Loaded(val list: List<TopicModel>) : TopicViewState()

    class Error {
        object NetworkError : TopicViewState()
        object UnexpectedError : TopicViewState()
    }

    object SuccessOperation : TopicViewState()
}
