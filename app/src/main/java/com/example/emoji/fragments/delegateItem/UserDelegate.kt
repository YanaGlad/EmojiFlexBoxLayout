package com.example.emoji.fragments.delegateItem

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.emoji.R
import com.example.emoji.customview.EmojiFactory
import com.example.emoji.customview.EmojiMessageView
import com.example.emoji.databinding.MessageItemBinding
import com.example.emoji.model.UserModel


class UserDelegate internal constructor() : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        UserViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false)
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
        val view: View
    ) : RecyclerView.ViewHolder(view) {

        private lateinit var item: UserModel
        private val binding = MessageItemBinding.bind(view)

        private fun defaultAlertDialog(reactions: ArrayList<String>, emojiView: EmojiMessageView) {
            val gridView = GridView(view.context)
            gridView.apply {
                adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_list_item_1,
                    reactions
                )
                numColumns = 5
            }

            val alert: AlertDialog = AlertDialog.Builder(view.context)
                .setView(gridView)
                .create()

            gridView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    emojiView.addNewEmoji((parent.getChildAt(position) as TextView).text.toString())
                    reactions.removeAt(position)
                    alert.hide()
                }

            if (reactions.isNotEmpty())
                alert.show()
            else Toast.makeText(view.context, "Кончились эмоджи!", Toast.LENGTH_SHORT).show()
        }

        @SuppressLint("SetTextI18n")
        fun bind(userModel: UserModel) {
            item = userModel
            binding.messsageView.name = userModel.name
            binding.messsageView.messageText = userModel.message
            binding.messsageView.image = userModel.picture
            val reactions: ArrayList<String> = EmojiFactory().getEmoji()
            binding.messsageView.showAlertDialog =
                { defaultAlertDialog(reactions, binding.messsageView) }
        }

    }


}