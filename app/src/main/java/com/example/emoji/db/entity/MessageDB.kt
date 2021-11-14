package com.example.emoji.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.emoji.api.model.Reaction

@Entity(tableName = "message_table")
data class MessageDB(
    @PrimaryKey
    val id: Int,
    val topicName: String,
    val authorId: Int,
    val authorName: String,
    val avatar_url: String,
    val content: String,
    val time: Long,
    val is_me_message : Boolean
)