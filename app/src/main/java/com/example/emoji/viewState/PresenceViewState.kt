package com.example.emoji.viewState

import com.example.emoji.api.model.Presence

/**
 * @author y.gladkikh
 */
sealed class PresenceViewState {
    object Loading : PresenceViewState()

    class Loaded(val presence: Presence) : PresenceViewState()

    class Error {
        object NetworkError : PresenceViewState()
        object UnexpectedError : PresenceViewState()
    }

    object SuccessOperation : PresenceViewState()
}
