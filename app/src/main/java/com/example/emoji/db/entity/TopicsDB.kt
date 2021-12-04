package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author y.gladkikh
 */
@Entity(tableName = "topics_table")
data class TopicsDB(
    @PrimaryKey
    val title : String,
    val maxMessageId : Int,
    val streamId : Int
)
