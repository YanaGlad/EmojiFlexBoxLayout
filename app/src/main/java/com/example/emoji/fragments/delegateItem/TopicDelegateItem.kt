package com.example.emoji.fragments.delegateItem

import com.example.emoji.model.TopicModel

data class TopicDelegateItem(
    val id : Int,
    val topicModel: TopicModel
) : DelegateItem {
    override fun content(): Any = topicModel
    override fun id(): Int = id
    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as TopicDelegateItem).topicModel == topicModel
    }
}
