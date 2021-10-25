package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.DateModel


data class DateDelegateItem(
    val id : Int,
    val value : DateModel
): DelegateItem {
    override fun content(): Any = value
    override fun id(): Int = id
}
