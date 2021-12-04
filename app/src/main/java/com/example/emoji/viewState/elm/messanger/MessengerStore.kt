package com.example.emoji.viewState.elm.messanger

import vivid.money.elmslie.core.ElmStoreCompat

/**
 * @author y.gladkikh
 */
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