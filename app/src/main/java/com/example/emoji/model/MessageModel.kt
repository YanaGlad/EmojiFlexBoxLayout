package com.example.emoji.model

import com.example.emoji.customview.EmojiFactory

data class MessageModel(
    val name : String,
    val message : String,
    val picture : Int,
    val date : String,
    val month : String,
    val isMe : Boolean,

){
   private val reactions: ArrayList<String> = EmojiFactory().getEmoji()
   val reactList: ArrayList<Reaction> = arrayListOf()

   init {
       initReactions()
   }

    private fun initReactions() {
        for (reaction in reactions) {
            reactList.add(Reaction(1, reaction))
        }
    }

    fun updateMessageEmojiList(pos : Int){
        reactList.removeAt(pos)
        initReactions()
    }
}