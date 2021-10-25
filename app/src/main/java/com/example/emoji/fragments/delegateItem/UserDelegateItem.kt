package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.MessageModel


data class UserDelegateItem(
    val id : Int,
    val messageModel: MessageModel
) : DelegateItem {
    override fun content(): Any = messageModel
    override fun id(): Int = id
}