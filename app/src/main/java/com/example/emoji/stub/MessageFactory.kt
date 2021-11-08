package com.example.emoji.stub

import com.example.emoji.model.MessageModel

class MessageFactory {
    fun getMessages() : ArrayList<MessageModel> = arrayListOf(
        MessageModel(1,
            name = "Yana Glad",
            message = "Hello!",
            picture = "https://cdn1.img.sputniknews-uz.com/img/746/58/7465859_9:-1:3631:2048_1920x0_80_0_0_88c44b55416429222099232187ac7800.jpg",
            "17",
            "mar",
            false,
            listOf()
        ))

}
