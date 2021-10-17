package com.example.emoji.fragments.delegateItem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.R
import com.example.emoji.customview.EmojiFactory
import com.example.emoji.databinding.MessageItemBinding
import com.example.emoji.fragments.BottomSheetFragment
import com.example.emoji.model.Reaction
import com.example.emoji.model.UserModel


class UserDelegate constructor(private val supportFragmentManager: FragmentManager) : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        UserViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false),
            supportFragmentManager = supportFragmentManager
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as UserViewHolder).bind(item.content() as UserModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean =
        item is UserDelegateItem

    class UserViewHolder(
        val view: View,
        val supportFragmentManager: FragmentManager
    ) : RecyclerView.ViewHolder(view) {

        private lateinit var item: UserModel
        private val binding = MessageItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bind(userModel: UserModel) {
            item = userModel

            binding.messsageView.apply {
                isMy = userModel.isMe
                name = userModel.name
                messageText = userModel.message
                image = userModel.picture
            }

            val reactions: ArrayList<String> = EmojiFactory().getEmoji()
            val reactList: ArrayList<Reaction> = arrayListOf()

            for (reaction in reactions) {
                reactList.add(Reaction(1, reaction))
            }

            val bottomSheet = BottomSheetFragment(reactList) { reaction, position ->
                binding.messsageView.addNewEmoji(reaction.emoji)
                reactions.removeAt(position)
            }

            binding.messsageView.showAlertDialog = { bottomSheet.show(supportFragmentManager, "bottom_tag") }

            binding.messsageView.messageView.longPressed = Runnable {
                bottomSheet.show(supportFragmentManager, "bottom_tag")
            }
        }
    }
}