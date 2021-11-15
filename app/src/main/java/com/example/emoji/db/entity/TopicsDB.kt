package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics_table")
data class TopicsDB(
    @PrimaryKey
    val id : Int
)