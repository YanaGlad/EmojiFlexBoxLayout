package com.example.emoji.viewState

import com.example.emoji.api.model.User

/**
 * @author y.gladkikh
 */
sealed class UserViewState {
    object Loading : UserViewState()

    class Loaded(val user: User) : UserViewState()

    class Error {
        object NetworkError : UserViewState()
        object UnexpectedError : UserViewState()
    }

    object SuccessOperation : UserViewState()
}
