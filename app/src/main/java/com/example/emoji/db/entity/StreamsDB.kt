package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author y.gladkikh
 */
@Entity(tableName = "stream_table")
data class StreamsDB(
    val id: Int,
    @PrimaryKey
    val title: String,
    val subscribed: Boolean,
    var clicked: Boolean,
)
