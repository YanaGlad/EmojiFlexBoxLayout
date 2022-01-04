package com.example.emoji.dataprovider

import com.example.emoji.model.UserModel
import io.reactivex.Flowable

interface LocalUserDataProvider {
    fun insertUser(item: UserModel)
    fun insertAllUsers(items: List<UserModel>)
    fun getAllUsers(): Flowable<List<UserModel>>
    fun deleteUser(item: UserModel)
    fun updateUser(item: UserModel)
}
