package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author y.gladkikh
 */
@Entity(tableName = "reactions_table")
data class ReactionsDB(
    @PrimaryKey
    val id: Int,
    val code: String,
    val name: String,
    val userId: Int,
)
