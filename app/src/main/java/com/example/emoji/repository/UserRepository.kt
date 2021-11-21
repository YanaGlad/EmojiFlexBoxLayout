package com.example.emoji.repository

import com.example.emoji.api.model.Presence
import com.example.emoji.api.model.User
import io.reactivex.Single

interface UserRepository {
    fun getMyUser(): Single<User>
    fun getAllUsers(): Single<List<User>>
    fun getUserPresence(userId: Int): Single<Presence>
}
