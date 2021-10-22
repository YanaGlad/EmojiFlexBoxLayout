package com.example.emoji.model

data class MessageModel(
    val name : String,
    val message : String,
    val picture : Int,
    val date : String,
    val month : String,
    val isMe : Boolean,
    val listReactions : MutableSet<Reaction>

)