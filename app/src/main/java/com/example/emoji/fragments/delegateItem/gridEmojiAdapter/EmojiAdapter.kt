package com.example.emoji.fragments.delegateItem.gridEmojiAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.api.model.codeToEmoji
import com.example.emoji.databinding.EmojiItemBinding
import com.example.emoji.model.Reaction

/**
 * @author y.gladkikh
 */
class EmojiAdapter internal constructor(private val clickListener: OnEmojiClickListener) :
    ListAdapter<Reaction, EmojiAdapter.EmojiViewHolder>(OperationDiffUtil()) {

    interface OnEmojiClickListener {
        fun onEmojiClick(reaction: Reaction, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val holder = EmojiViewHolder(
            EmojiItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
        holder.binding.emoji.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                clickListener.onEmojiClick(getItem(holder.adapterPosition), holder.adapterPosition)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OperationDiffUtil : DiffUtil.ItemCallback<Reaction>() {
        override fun areItemsTheSame(
            oldItem: Reaction,
            newItem: Reaction,
        ): Boolean =
            oldItem.emoji == newItem.emoji

        override fun areContentsTheSame(
            oldItem: Reaction,
            newItem: Reaction,
        ): Boolean =
            oldItem == newItem
    }

    class EmojiViewHolder(
        val binding: EmojiItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(reaction: Reaction) {
            binding.emoji.text = reaction.emoji
        }
    }
}
