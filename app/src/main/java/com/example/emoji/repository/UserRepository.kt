package com.example.emoji.repository

import com.example.emoji.api.Api
import com.example.emoji.api.model.User
import io.reactivex.Single

class UserRepository(private val apiService: Api) {
    fun getMyUser(): Single<User> {
        return apiService.getMyUser()
            .map {
                User(
                    id = it.id,
                    full_name = it.full_name,
                    email = it.email,
                    avatar_url = it.avatar_url,
                    is_active = it.is_active
                )
            }
    }

    fun getAllUsers() : Single<List<User>>{
        return apiService.getAllUsers()
            .flatMap { it ->
                val mappedList = it.members.map {
                    User(
                        id = it.id,
                        full_name = it.full_name,
                        email = it.email,
                        avatar_url = it.avatar_url,
                        is_active = it.is_active
                    )
                }
                Single.just(mappedList)
            }
    }
}