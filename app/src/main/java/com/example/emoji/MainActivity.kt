package com.example.emoji

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.emoji.customview.EmojiFactory
import com.example.emoji.customview.EmojiMessageView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: com.example.emoji.databinding.ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = com.example.emoji.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emojiView = EmojiMessageView(this).apply {
            name = "Yana Glad"
            messageText = getString(R.string.how_are_u)
            image = R.drawable.ic_user
            reactions = EmojiFactory().getEmoji()
            onPlusClickLister = View.OnClickListener { it?.isSelected = !it?.isSelected!! }
            showAlertDialog = { defaultAlertDialog() }

        }
        binding.linear.addView(emojiView)

        val emojiView2 = EmojiMessageView(this).apply {
            name = "Ivanov Ivan"
            messageText = "Lalalalalallaallal alalalaaaa aaaaaaaaaaaaaaaa aaaaaaaaaa aaaaaaaaaa"
            image = R.drawable.ic_user
            reactions = EmojiFactory().getAnotherEmoji()
            onPlusClickLister = View.OnClickListener { it?.isSelected = !it?.isSelected!! }
            showAlertDialog = { defaultAlertDialog() }

        }
        binding.linear.addView(emojiView2)
    }

    private fun EmojiMessageView.defaultAlertDialog() {
        val gridView = GridView(context)
        gridView.apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, reactions)
            numColumns = 5
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(gridView)
        val alert = builder.create()

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                setupText.value = (parent.getChildAt(position) as TextView).text.toString()
                reactions.removeAt(position)
                alert.hide()
            }

        alert.show()
    }
}