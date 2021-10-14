package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.UserModel


data class UserDelegateItem(
    val userModel: UserModel
) : DelegateItem {
    override fun content(): Any = userModel
    override fun id(): Any = userModel.name
}