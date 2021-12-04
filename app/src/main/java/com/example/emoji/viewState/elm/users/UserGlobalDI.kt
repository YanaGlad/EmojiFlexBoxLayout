package com.example.emoji.viewState.elm.users

import com.example.emoji.repository.UserRepository
import com.example.emoji.viewState.elm.laod.LoadUsers
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class UserGlobalDI @Inject constructor(val repository: UserRepository) {

    private val loadUsers by lazy { LoadUsers(repository) }

    private val actor by lazy { UserActor(loadUsers) }

    val elmStoreFactory by lazy { UserStore(actor) }
}