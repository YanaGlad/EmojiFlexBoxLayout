package com.example.emoji.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TopicModel(
    val title : String,
    val messagesCount : Int
) : Parcelable