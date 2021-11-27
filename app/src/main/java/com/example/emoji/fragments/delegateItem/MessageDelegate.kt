package com.example.emoji.fragments.delegateItem

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.R
import com.example.emoji.databinding.MessageItemBinding
import com.example.emoji.model.MessageModel
import com.example.emoji.support.loadImage


class MessageDelegate constructor(
    private val onUserClick: OnUserDelegateClickListener,
    private val onEmojiClick: (String, Int) -> Unit = { _, _ -> run {} },
    private val onLoadMoreCallback: (Int) -> Unit,
) : AdapterDelegate {

    fun interface OnUserDelegateClickListener {
        fun onUserClick(item: MessageModel, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        UserViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false),
            onUserClick = onUserClick,
            onEmojiClick = onEmojiClick
        )


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
    ) {
//        if (position % 30 == 0) {
//            onLoadMoreCallback((item.content() as MessageModel).id)
//        }
        (holder as UserViewHolder).bind(item.content() as MessageModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is MessageDelegateItem

    class UserViewHolder(
        val view: View,
        private val onUserClick: OnUserDelegateClickListener,
        val onEmojiClick: (String, Int) -> Unit,
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

                val emojiIcons: ArrayList<String> = arrayListOf()
                val emojiCounts: ArrayList<Int> = arrayListOf()
                var size = 0
                for ((emoji, count) in messageModel.countedReactions) {
                    Log.d("check", "${messageModel.message}")
                    if (emoji != "zulip" && emoji != "0031-20e3" && emoji != "0037-20e3" && emoji != "0033-20e3") {
                        emojiIcons.add(String(Character.toChars(Integer.parseInt(emoji, 16))))
                        emojiCounts.add(count)
                        size++
                    }
                }

                for (i in 0 until size) {
                    addNewEmoji(
                        emojiCounts[i], emojiIcons[i],
                        messageModel.listReactions[i].clicked) { onEmojiClick(emojiIcons[i], messageModel.id) }
                }
            }

            binding.message.messageView.longPressed = Runnable {
                onUserClick.onUserClick(messageModel, adapterPosition)
            }
        }
    }
}
