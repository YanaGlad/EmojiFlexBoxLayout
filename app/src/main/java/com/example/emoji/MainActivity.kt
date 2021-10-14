package com.example.emoji

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.emoji.customview.EmojiFactory
import com.example.emoji.customview.EmojiMessageView
import com.example.emoji.customview.EmojiView
import com.example.emoji.databinding.ActivityMainBinding
import com.example.emoji.model.User


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reactions1: ArrayList<String> = EmojiFactory().getEmoji()

        val emojiView = EmojiMessageView(this).apply {
            name = "Yana Glad"
            messageText = "Привет!"
            image = R.drawable.ic_user
            showAlertDialog = { defaultAlertDialog(reactions1,this) }

        }

        emojiView.name = "Anonymous"

        binding.linear.addView(emojiView)

        val user2 = User(
            name = "Ivanov Ivan",
            message = "Привет, как дела? How are you?",
            picture = R.drawable.spaceman
        )

        val reactions2: ArrayList<String> = EmojiFactory().getEmoji()

        val emojiView2 = EmojiMessageView(this).apply {
            name = user2.name
            messageText = "Привет, как дела? How are you?"
            image = user2.picture
            showAlertDialog = { defaultAlertDialog(reactions2, this) }
        }
        binding.linear.addView(emojiView2)
    }


    private fun  defaultAlertDialog(reactions: ArrayList<String>, emojiView: EmojiMessageView) {
        val gridView = GridView(this)
        gridView.apply {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                reactions
            )
            numColumns = 5
        }

        val alert : AlertDialog = AlertDialog.Builder(this)
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
        else Toast.makeText(this, "Кончились эмоджи!" , Toast.LENGTH_SHORT).show()
    }
}