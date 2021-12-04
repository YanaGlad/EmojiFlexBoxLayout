package com.example.emoji.fragments.delegateItem

/**
 * @author y.gladkikh
 */
interface DelegateItem {
    fun content() : Any
    fun id() : Int
    fun compareToOther(other : DelegateItem) : Boolean
}
