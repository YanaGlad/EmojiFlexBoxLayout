package com.example.emoji.viewState.elm

import com.example.emoji.repository.MessageRepository
import com.example.emoji.viewState.elm.laod.LoadMessages
import javax.inject.Inject

class GlobalDI @Inject constructor(val repository: MessageRepository) {


    val loadMessages by lazy { LoadMessages(repository) }

    val actor by lazy { MessengerActor(loadMessages) }

    val elmStoreFactory by lazy { MessengerStore(actor) }

}