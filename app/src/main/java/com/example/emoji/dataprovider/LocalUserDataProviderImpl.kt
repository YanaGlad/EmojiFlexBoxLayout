package com.example.emoji.dataprovider

import com.example.emoji.db.dao.UserDao
import com.example.emoji.db.entity.UserDB
import com.example.emoji.model.UserModel
import io.reactivex.Flowable
import javax.inject.Inject

class LocalUserDataProviderImpl @Inject constructor(private val dao: UserDao) : LocalUserDataProvider {

    override fun insertUser(item: UserModel) {
        dao.insertUser(
            UserDB(
                id = item.id,
                name = item.name,
                email = item.email,
                picture = item.picture
            )
        )
    }

    override fun insertAllUsers(items: List<UserModel>) {
        dao.insertAllUsers(
            items.map {
                UserDB(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    picture = it.picture
                )
            }
        )
    }

    override fun getAllUsers(): Flowable<List<UserModel>> =
        dao.getAllUsers().flatMap { it ->
            val mappedList = it.map {
                UserModel(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    picture = it.picture
                )
            }
            Flowable.just(mappedList)
        }

    override fun deleteUser(item: UserModel) {
        dao.deleteUser(
            UserDB(
                id = item.id,
                name = item.name,
                email = item.email,
                picture = item.picture
            )
        )
    }

    override fun updateUser(item: UserModel) {
        dao.updateUser(
            UserDB(
                id = item.id,
                name = item.name,
                email = item.email,
                picture = item.picture
            )
        )
    }
}
