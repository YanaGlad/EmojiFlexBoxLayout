package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class MessageDB(
    @PrimaryKey
    val id: Int,
    val topicName: String,
    val authorId: Int,
    val authorName: String,
    val avatarUrl: String,
    val content: String,
    val time: Long,
    val isMeMessage: Boolean,
)
