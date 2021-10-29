package com.example.emoji.fragments.delegateItem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.R
import com.example.emoji.databinding.StreamItemBinding
import com.example.emoji.model.StreamModel

class StreamDelegate(private val onStreamClick: OnStreamDelegateClickListener) : AdapterDelegate { //

    interface OnStreamDelegateClickListener {
        fun onStreamClick(item: StreamModel, position: Int): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        StreamViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.stream_item, parent, false),
            onStreamClick = onStreamClick
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as StreamViewHolder).bind(item.content() as StreamModel)
    }


    override fun isOfViewType(item: DelegateItem): Boolean =
        item is StreamDelegateItem

    class StreamViewHolder(val view: View, private val onStreamClick: OnStreamDelegateClickListener) :
        RecyclerView.ViewHolder(view) {

        private val binding = StreamItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(streamModel: StreamModel) {
            binding.title.text = streamModel.title

            if (streamModel.clicked)
                binding.viewTopics.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_view_opened))
            else binding.viewTopics.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_view))

            itemView.setOnClickListener {
                onStreamClick.onStreamClick(streamModel, adapterPosition)
            }
        }
    }
}