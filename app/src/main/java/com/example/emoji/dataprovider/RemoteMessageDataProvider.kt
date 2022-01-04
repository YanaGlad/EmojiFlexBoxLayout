package com.example.emoji.dataprovider

import com.example.emoji.api.model.Message
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteMessageDataProvider {
    fun getMessages(queryMap: Map<String, String>): Single<List<Message>>
    fun sendMessage(queryMap: Map<String, String>): Completable
}
