package com.example.emoji.dataprovider

import com.example.emoji.api.Api
import com.example.emoji.api.model.Presence
import com.example.emoji.api.model.User
import io.reactivex.Single
import javax.inject.Inject

class RemoteUserDataProviderImpl @Inject constructor(private val api: Api) : RemoteUserDataProvider {

    override fun getMyUser(): Single<User> =
        api.getMyUser().flatMap {
            val mapped = User(
                id = it.id,
                fullName = it.fullName,
                email = it.email,
                avatarUrl = it.avatarUrl,
                isActive = it.isActive
            )
            Single.just(mapped)
        }

    override fun getAllUsers(): Single<List<User>> =
        api.getAllUsers().flatMap { it ->
            val mapped = it.members.map {
                User(
                    id = it.id,
                    fullName = it.fullName,
                    email = it.email,
                    avatarUrl = it.avatarUrl,
                    isActive = it.isActive
                )
            }
            Single.just(mapped)
        }

    override fun getUserPresence(id: Int): Single<Presence> {
        return api.getUserPresence(id).flatMap {
            Single.just(
                Presence(
                    userId = id,
                    time = it.presence.oneResponse.timestamp,
                    status = it.presence.oneResponse.status
                )
            )
        }
    }
}
