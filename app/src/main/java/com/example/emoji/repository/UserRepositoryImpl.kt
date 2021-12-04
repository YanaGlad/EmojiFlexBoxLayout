package com.example.emoji.repository

import com.example.emoji.api.model.Presence
import com.example.emoji.api.model.User
import com.example.emoji.dataprovider.LocalUserDataProvider
import com.example.emoji.dataprovider.RemoteUserDataProvider
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author y.gladkikh
 */
class UserRepositoryImpl @Inject constructor(
    private val remote: RemoteUserDataProvider,
    private val local: LocalUserDataProvider,
) : UserRepository {

    override fun getMyUser(): Single<User> {
        return remote.getMyUser()
            .map {
                User(
                    id = it.id,
                    fullName = it.fullName,
                    email = it.email,
                    avatarUrl = it.avatarUrl,
                    isActive = it.isActive
                )
            }
    }

    override fun getAllUsers(): Single<List<User>> {
        return remote.getAllUsers()
    }

    override fun getUserPresence(userId: Int): Single<Presence> =
        remote.getUserPresence(userId)
}
