package com.example.emoji.stub

import com.example.emoji.R
import com.example.emoji.model.UserModel

class UserFactory {
    fun getUsers() : ArrayList<UserModel> = arrayListOf(
        UserModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false
        ),
        UserModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar",
            false
        ),
        UserModel(
            name = "Darrell Steward",
            message = "Привет! lorem ipsum test message",
            picture = R.drawable.spaceman,
            "11",
            "feb",
            false
        ),
        UserModel(
            name = "Алексей",
            message = "Привет, что вчера задали по матану?",
            picture = R.drawable.spaceman,
            "12",
            "feb",
            false
        )
        ,
        UserModel(
            name = "Иван Иванов",
            message = "Демидович 3512-3515",
            picture = R.drawable.kopatich,
            "12",
            "feb",
            false
        ),
        UserModel(
            name = "Алексей",
            message = "Спасибо!",
            picture = R.drawable.spaceman,
            "12",
            "feb",
            false
        )
        , UserModel(
            name = "Yana Glad",
            message = ":) Первое сообщение ",
            picture = R.drawable.spaceman,
            "1",
            "jan",
            false
        )
    )


}