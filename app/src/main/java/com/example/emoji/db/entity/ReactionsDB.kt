package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity(tableName = "reactions_table")
data class ReactionsDB(
    @PrimaryKey
    val id: Int,
    val code: String,
    val name: String,
    val userId: Int,
)