package com.example.emoji.dataprovider

import com.example.emoji.api.model.Message
import io.reactivex.Flowable

interface LocalMessageDataProvider {
    fun insertMessage(item : Message)
    fun insertAllMessages(items : List<Message>)
    fun getAllMessages() : Flowable<List<Message>>
    fun deleteMessage(item : Message)
    fun updateMessage(item : Message)
}
