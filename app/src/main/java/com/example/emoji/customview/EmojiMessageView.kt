package com.example.emoji.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.emoji.R
import com.example.emoji.support.rect
import com.example.emoji.support.height
import com.example.emoji.support.layout
import com.example.emoji.support.width

class EmojiMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes) {

    private val avatar: RoundedImageView
    private val messageView: MessageLayout
    private val flexboxLayout: FlexBoxLayout
    private val avatarRect = Rect()
    private val tvMessageRect = Rect()
    private val flexboxRect = Rect()

    private var setupText: MutableLiveData<String> = MutableLiveData<String>()
    private var list = EmojiFactory().getEmoji()

    private fun showAlertDialog() {
        val gridView = GridView(context)
        gridView.apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)
            numColumns = 5
        }


        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(gridView)
        val alert = builder.create()

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                setupText.value = (parent.getChildAt(position) as TextView).text.toString()
                list.removeAt(position)
                alert.hide()
            }

        alert.show()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.emoji_view_group, this, true)

        avatar = findViewById(R.id.avatar)
        messageView = findViewById(R.id.message_title)
        flexboxLayout = findViewById(R.id.flexbox)

        setupText.value = ""

        val plus = EmojiView(context)
        plus.background = getDrawable(context, R.drawable.bg_custom_text_view)
        plus.tap_count = 0
        plus.text = "  +"

        plus.setOnClickListener {
            it.isSelected = !it.isSelected
        }

        flexboxLayout.addView(plus)
        flexboxLayout.requestLayout()

        setupText.observe((context as LifecycleOwner), {
            val view = EmojiView(context)

            view.apply {
                text = setupText.value!!
                if (setupText.value?.length!! >= 1) {
                    background = getDrawable(context, R.drawable.bg_custom_text_view)
                    tap_count = 1
                    setOnClickListener {
                        it.isSelected = !it.isSelected
                    }
                    flexboxLayout.addView(this)
                    flexboxLayout.requestLayout()
                }
            }
        })


        (flexboxLayout.getChildAt(0) as EmojiView).apply {
            background = getDrawable(context, R.drawable.bg_custom_text_view)
            tap_count = 0
            setOnClickListener {
                it.isSelected = !it.isSelected
                if (list.isNotEmpty())
                    showAlertDialog()
                it.isSelected = !it.isSelected
            }
        }


        for (i in 1 until flexboxLayout.childCount) {
            (flexboxLayout.getChildAt(i) as EmojiView).apply {
                background = getDrawable(context, R.drawable.bg_custom_text_view)
                setOnClickListener {
                    it.isSelected = !it.isSelected
                }
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)

        measureChildWithMargins(avatar, widthMeasureSpec, 0, heightMeasureSpec, 0)

        measureChildWithMargins(
            messageView,
            widthMeasureSpec,
            avatar.width(),
            heightMeasureSpec,
            0
        )

        val msgHeight = messageView.height()
        measureChildWithMargins(
            flexboxLayout,
            widthMeasureSpec,
            avatar.width(),
            heightMeasureSpec,
            msgHeight
        )

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(
                maxOf(avatar.height(), msgHeight + flexboxLayout.height()),
                heightMeasureSpec
            )
        )
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        avatar.layout(avatar.rect(avatarRect, 0, 0))
        messageView.layout(messageView.rect(tvMessageRect, avatar.right, 0))

        flexboxLayout.layout(
            flexboxLayout.rect(
                flexboxRect,
                20,
                messageView.bottom - 50, r - 150
            )
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)


}