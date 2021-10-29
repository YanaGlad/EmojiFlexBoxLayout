package com.example.emoji.stub

import com.example.emoji.R
import com.example.emoji.model.MessageModel
import com.example.emoji.model.StreamModel
import com.example.emoji.model.TopicModel
import com.example.emoji.model.UserModel

class MessageFactory {
    fun getUsers() : ArrayList<UserModel> = arrayListOf(
        UserModel(
            name = "Yana Glad",
            email = "yana.glad@mail.ru",
            picture = R.drawable.spaceman
        ),
        UserModel(
            name = "Darrel Steward",
            email = "darrel.steward@mail.ru",
            picture = R.drawable.kopatich
        ),
        UserModel(
            name = "Anonymous",
            email = "anon.anon@mail.ru",
            picture = R.drawable.ic_launcher_background
        )
    )

    fun getMessages() : ArrayList<MessageModel> = arrayListOf(
        MessageModel(1,
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false,
            mutableSetOf()
        ),
        MessageModel(2,
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false,
            mutableSetOf()
        ),
        MessageModel(3,
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false,
            mutableSetOf()
        ),
        MessageModel(4,
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false,
            mutableSetOf()
        ),
        MessageModel(5,
            name = "Darrell Steward",
            message = "Привет! lorem ipsum test message",
            picture = R.drawable.spaceman,
            "11",
            "feb",
            false,
            mutableSetOf()
        ),
        MessageModel(6,
            name = "Алексей",
            message = "Привет, что вчера задали по матану?",
            picture = R.drawable.spaceman,
            "12",
            "feb",
            false,
            mutableSetOf()
        )
        ,
        MessageModel(7,
            name = "Иван Иванов",
            message = "Демидович 3512-3515",
            picture = R.drawable.kopatich,
            "12",
            "feb",
            false,
            mutableSetOf()
        ),
        MessageModel(8,
            name = "Алексей",
            message = "Спасибо!",
            picture = R.drawable.spaceman,
            "12",
            "feb",
            false,
            mutableSetOf()
        )
        , MessageModel(9,
            name = "Yana Glad",
            message = ":) Первое сообщение ",
            picture = R.drawable.spaceman,
            "1",
            "jan",
            false,
            mutableSetOf()
        )

        , MessageModel(10,
            name = "Person N",
            message = "Test test test ",
            picture = R.drawable.spaceman,
            "18",
            "mar",
            false,
            mutableSetOf()
        )
    )


    fun getStreamsStub() = arrayListOf(
        StreamModel(
            id = 1,
            title = "#general",
            topics = listOf(
                TopicModel("#testing", 1345),
                TopicModel("#bruh", 234)
            ),
            subscribed = true,
            clicked = false
        ),
        StreamModel(
            id = 2,
            title = "#Development",
            topics = listOf(
                TopicModel("#Android", 4605),
                TopicModel("#iOS", 4522)
            ), subscribed = true, clicked = false
        ),
        StreamModel(
            id =3,
            title ="#Design",
            topics = listOf(
                TopicModel("#figma", 123),
                TopicModel("#html", 12234),
                TopicModel("#xml", 2342),
                TopicModel("#css", 555)
            ),
            subscribed = true,
            clicked = false
        ),
        StreamModel(id =4,
            title =  "#PR",
            topics = listOf(),
            subscribed = false,
            clicked = false
        )
    )
}