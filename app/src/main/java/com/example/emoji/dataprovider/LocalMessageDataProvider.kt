package com.example.emoji.dataprovider

import androidx.room.*
import com.example.emoji.db.entity.MessageDB
import com.example.emoji.model.MessageModel
import io.reactivex.Flowable

interface LocalMessageDataProvider {
    fun insertMessage(item : MessageModel)
    fun insertAllMessages(items : List<MessageModel>)
    fun getAllMessages() : Flowable<List<MessageModel>>
    fun deleteMessage(item : MessageModel)
    fun updateMessage(item : MessageModel)
}