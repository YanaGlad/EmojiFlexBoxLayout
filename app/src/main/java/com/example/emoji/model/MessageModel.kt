package com.example.emoji.model

data class MessageModel(
    val id : Int,
    val name : String,
    val message : String,
    val picture : Int, //TODO UserModel
    val date : String,
    val month : String,
    val isMe : Boolean,
    val listReactions : Set<Reaction>

)