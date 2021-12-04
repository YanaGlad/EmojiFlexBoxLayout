package com.example.emoji.viewState.elm.laod

import com.example.emoji.api.model.Presence
import com.example.emoji.api.model.User
import com.example.emoji.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class LoadUsers @Inject constructor(
    private val repo: UserRepository,
) {
    fun getMyUser(): Single<User> = repo.getMyUser()
    fun getAllUsers(): Single<List<User>> = repo.getAllUsers()
    fun getUserPresence(userId: Int): Single<Presence> = repo.getUserPresence(userId)
}