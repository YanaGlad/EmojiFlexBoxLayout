package com.example.emoji.stub

import com.example.emoji.R
import com.example.emoji.model.MessageModel

class UserFactory {
    fun getUsers() : ArrayList<MessageModel> = arrayListOf(
        MessageModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false
        ),
        MessageModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false
        ),
        MessageModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false
        ),
        MessageModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false
        ),
        MessageModel(
            name = "Darrell Steward",
            message = "Привет! lorem ipsum test message",
            picture = R.drawable.spaceman,
            "11",
            "feb",
            false
        ),
        MessageModel(
            name = "Алексей",
            message = "Привет, что вчера задали по матану?",
            picture = R.drawable.spaceman,
            "12",
            "feb",
            false
        )
        ,
        MessageModel(
            name = "Иван Иванов",
            message = "Демидович 3512-3515",
            picture = R.drawable.kopatich,
            "12",
            "feb",
            false
        ),
        MessageModel(
            name = "Алексей",
            message = "Спасибо!",
            picture = R.drawable.spaceman,
            "12",
            "feb",
            false
        )
        , MessageModel(
            name = "Yana Glad",
            message = ":) Первое сообщение ",
            picture = R.drawable.spaceman,
            "1",
            "jan",
            false
        )

        , MessageModel(
            name = "Person N",
            message = "Test test test ",
            picture = R.drawable.spaceman,
            "18",
            "mar",
            false
        )
    )


}