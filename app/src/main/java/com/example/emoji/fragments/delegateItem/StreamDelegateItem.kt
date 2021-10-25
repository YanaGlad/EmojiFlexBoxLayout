package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.StreamModel

class StreamDelegateItem (
    val id : Int,
    private val streamModel: StreamModel
) : DelegateItem {
    override fun content(): Any = streamModel
    override fun id(): Int = streamModel.hashCode()
}
