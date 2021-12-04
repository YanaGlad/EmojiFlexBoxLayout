package com.example.emoji.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author y.gladkikh
 */
@Parcelize
data class UserModel(
    val id : Int,
    val name : String,
    val email : String,
    val picture : String
) : Parcelable
