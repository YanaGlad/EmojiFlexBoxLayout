package com.example.emoji.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDB(
    @PrimaryKey
    val id : Int,
    val name : String,
    val email : String,
    val picture : String
)
