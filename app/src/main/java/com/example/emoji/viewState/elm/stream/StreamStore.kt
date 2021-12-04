package com.example.emoji.viewState.elm.stream

import vivid.money.elmslie.core.ElmStoreCompat

/**
 * @author y.gladkikh
 */
class StreamStore (
    private val actor: StreamActor
) {
    private val store by lazy {
        ElmStoreCompat(
            initialState = StreamState(),
            reducer = StreamReducer(),
            actor = actor
        )
    }
    fun provide() = store
}