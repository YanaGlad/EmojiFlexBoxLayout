package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.DateModel

/**
 * @author y.gladkikh
 */
data class DateDelegateItem(
    val id : Int,
    val value : DateModel
): DelegateItem {
    override fun content(): Any = value
    override fun id(): Int = id
    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as DateDelegateItem).value == content()
    }
}
