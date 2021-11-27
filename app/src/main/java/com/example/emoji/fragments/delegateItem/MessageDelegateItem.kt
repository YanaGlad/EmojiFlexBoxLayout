package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.MessageModel


data class MessageDelegateItem(
    val messageModel: MessageModel
) : DelegateItem {
    override fun content(): Any = messageModel
    override fun id(): Int = messageModel.id
    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as MessageDelegateItem).messageModel == this.messageModel
    }
}
