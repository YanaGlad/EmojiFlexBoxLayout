package com.example.emoji.fragments.delegateItem

interface DelegateItem {
    fun content() : Any
    fun id() : Int
    fun compareToOther(other : DelegateItem) : Boolean
}