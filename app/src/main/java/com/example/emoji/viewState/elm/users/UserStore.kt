package com.example.emoji.viewState.elm.users

import vivid.money.elmslie.core.ElmStoreCompat

/**
 * @author y.gladkikh
 */
class UserStore (
    private val actor: UserActor
) {
    private val store by lazy {
        ElmStoreCompat(
            initialState = UserState(),
            reducer = UserReducer(),
            actor = actor
        )
    }
    fun provide() = store
}