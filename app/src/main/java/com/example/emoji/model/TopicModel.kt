package com.example.emoji.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author y.gladkikh
 */
@Parcelize
class TopicModel(
    val title : String,
    val maxMessageId : Int
) : Parcelable
