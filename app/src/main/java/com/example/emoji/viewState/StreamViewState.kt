package com.example.emoji.viewState

import com.example.emoji.model.StreamModel

sealed class StreamViewState {
    object Loading : StreamViewState()

    class Loaded(val list: List<StreamModel>) : StreamViewState()

    class Error {
        object NetworkError : StreamViewState()
        object UnexpectedError : StreamViewState()
    }

    object SuccessOperation : StreamViewState()
}
