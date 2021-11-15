package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream_table")
data class StreamsDB(
    @PrimaryKey
    val id : Int
)