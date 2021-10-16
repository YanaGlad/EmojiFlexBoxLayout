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
            "mar"
        ),
        UserModel(
            name = "Yana Glad",
            message = "Hello!",
            picture = R.drawable.spaceman,
            "17",
            "mar"
        ),
        UserModel(
            name = "Yana Glad",
            message = "Hello! How re you? Как дела?",
            picture = R.drawable.spaceman,
            "18",
            "feb"
        ),
        UserModel(
            name = "Yana Glad",
            message = "Hello! How re you? Как дела?",
            picture = R.drawable.spaceman,
            "11",
            "feb"
        )
    )


}