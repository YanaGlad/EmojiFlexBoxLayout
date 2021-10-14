package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.DateModel


data class DateDelegateItem(
    val value : DateModel
): DelegateItem {
    override fun content(): Any = value
    override fun id(): Any = value.hashCode()
}
