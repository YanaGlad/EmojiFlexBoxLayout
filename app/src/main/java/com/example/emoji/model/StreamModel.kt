package com.example.emoji.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamModel(
    val id : Int,
    val title: String,
    var topics : List<TopicModel>,
    val subscribed : Boolean,
    var clicked : Boolean
 ) : Parcelable
