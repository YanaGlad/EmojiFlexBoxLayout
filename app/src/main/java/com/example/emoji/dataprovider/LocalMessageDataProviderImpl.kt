package com.example.emoji.dataprovider

import com.example.emoji.api.Api
import com.example.emoji.db.dao.MessageDao
import com.example.emoji.model.MessageModel
import io.reactivex.Flowable
import javax.inject.Inject

class LocalMessageDataProviderImpl @Inject constructor(private val dao: MessageDao)  : LocalMessageDataProvider {

    override fun insertMessage(item: MessageModel) {
        TODO("Not yet implemented")
    }

    override fun insertAllMessages(items: List<MessageModel>) {
        TODO("Not yet implemented")
    }

    override fun getAllMessages(): Flowable<List<MessageModel>> {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(item: MessageModel) {
        TODO("Not yet implemented")
    }

    override fun updateMessage(item: MessageModel) {
        TODO("Not yet implemented")
    }
}