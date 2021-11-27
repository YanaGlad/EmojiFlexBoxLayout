package com.example.emoji.viewState.elm

import vivid.money.elmslie.core.ElmStoreCompat

class MessengerStore(
    private val actor: MessengerActor
) {
    private val store by lazy {
        ElmStoreCompat(
            initialState = MessengerState(),
            reducer = MessengerReducer(),
            actor = actor
        )
    }
    fun provide() = store
}