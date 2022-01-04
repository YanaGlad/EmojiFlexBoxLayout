package com.example.emoji.fragments.delegateItem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.R
import com.example.emoji.databinding.TopicItemBinding
import com.example.emoji.model.TopicModel

/**
 * @author y.gladkikh
 */
data class TopicDelegate constructor(
    private val onTopicClick: OnTopicDelegateClickListener
    ) : AdapterDelegate {

    interface OnTopicDelegateClickListener {
        fun onTopicClick(item: TopicModel, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        TopicViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.topic_item, parent, false),
            onTopicClick = onTopicClick
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as TopicViewHolder).bind(item.content() as TopicModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is TopicDelegateItem


    class TopicViewHolder(
        val view: View,
        private val onTopicClick: OnTopicDelegateClickListener
    ) : RecyclerView.ViewHolder(view) {

        private val binding = TopicItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(topicModel: TopicModel) {
            binding.title.text = topicModel.title

            itemView.setOnClickListener {
                onTopicClick.onTopicClick(topicModel, adapterPosition)
            }
        }
    }
}
