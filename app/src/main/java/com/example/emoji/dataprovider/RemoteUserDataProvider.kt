package com.example.emoji.dataprovider

import com.example.emoji.api.model.Presence
import com.example.emoji.api.model.User
import io.reactivex.Single

interface RemoteUserDataProvider {
    fun getMyUser(): Single<User>
    fun getAllUsers(): Single<List<User>>
    fun getUserPresence(id: Int): Single<Presence>
}
