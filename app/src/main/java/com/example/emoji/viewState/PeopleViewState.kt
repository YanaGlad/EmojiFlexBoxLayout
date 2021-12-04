package com.example.emoji.viewState

import com.example.emoji.api.model.User

/**
 * @author y.gladkikh
 */
sealed class PeopleViewState {
    object Loading : PeopleViewState()

    class Loaded(val list: List<User>) : PeopleViewState()

    class Error {
        object NetworkError : PeopleViewState()
        object UnexpectedError : PeopleViewState()
    }

    object SuccessOperation : PeopleViewState()
}
