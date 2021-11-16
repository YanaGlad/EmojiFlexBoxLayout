package com.example.emoji.dataprovider

import androidx.room.*
import com.example.emoji.api.model.Message
import com.example.emoji.db.entity.MessageDB
import com.example.emoji.model.MessageModel
import io.reactivex.Flowable

interface LocalMessageDataProvider {
    fun insertMessage(item : Message)
    fun insertAllMessages(items : List<Message>)
    fun getAllMessages() : Flowable<List<Message>>
    fun deleteMessage(item : Message)
    fun updateMessage(item : Message)
}