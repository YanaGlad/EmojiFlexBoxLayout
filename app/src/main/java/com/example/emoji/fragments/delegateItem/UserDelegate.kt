package com.example.emoji.fragments.delegateItem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.R
import com.example.emoji.databinding.MessageItemBinding
import com.example.emoji.model.MessageModel
import com.example.emoji.support.loadImage


class UserDelegate constructor(private val onUserClick: OnUserDelegateClickListener) : AdapterDelegate {

    fun interface OnUserDelegateClickListener {
        fun onUserClick(item: MessageModel, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        UserViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false),
            onUserClick = onUserClick
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
    ) {
        (holder as UserViewHolder).bind(item.content() as MessageModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is UserDelegateItem

    class UserViewHolder(
        val view: View,
        private val onUserClick: OnUserDelegateClickListener,
    ) : RecyclerView.ViewHolder(view) {

        private val binding = MessageItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(messageModel: MessageModel) {

            binding.message.apply {
                setIsMy(messageModel.isMe)
                setUserName(messageModel.name)
                setMessage(messageModel.message)
                loadImage(itemView.context, messageModel.picture, avatar)
                clearFlexBox()
                initPlus(context)
                addCustomEmoji(plus)
                setupPlusClickListener { onUserClick.onUserClick(messageModel, adapterPosition) }

                for (emoji in messageModel.listReactions) {
                    addNewEmoji(emoji.emoji)
                }
            }

            binding.message.messageView.longPressed = Runnable {
                onUserClick.onUserClick(messageModel, adapterPosition)
            }
        }
    }
}
