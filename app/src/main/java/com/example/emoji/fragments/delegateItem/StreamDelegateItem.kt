package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.StreamModel

class StreamDelegateItem(
    private val streamModel: StreamModel
) : DelegateItem {
    override fun content(): Any = streamModel
    override fun id(): Int = streamModel.id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as StreamDelegateItem).streamModel == this.streamModel
    }
}
