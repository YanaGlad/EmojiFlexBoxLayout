package com.example.emoji.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.emoji.R
import com.example.emoji.support.height
import com.example.emoji.support.layout
import com.example.emoji.support.rect
import com.example.emoji.support.width

class EmojiMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes) {

    private val avatar: RoundedImageView
    val messageView: MessageLayout
    private val flexboxLayout: FlexBoxLayout
    private val avatarRect = Rect()
    private val tvMessageRect = Rect()
    private val flexboxRect = Rect()

    var messageText = ""
        set(value) {
            field = value
            requestLayout()
        }

    var name = "Yana Glad"
        set(value) {
            field = value
            requestLayout()
        }

    var image: Int = R.drawable.ic_launcher_background
        set(value) {
            field = value
            requestLayout()
        }

    var showAlertDialog: () -> Unit = {}

    val plus = EmojiView(context)

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiMessageView,
            defStyleAttrs,
            defStyleRes
        )
        initStyledAtttributes(typedArray)

        LayoutInflater.from(context).inflate(R.layout.emoji_view_group, this, true)
        avatar = findViewById(R.id.avatar)
        messageView = findViewById(R.id.message_title)

        setupValues()

        flexboxLayout = findViewById(R.id.flexbox)
        messageView.requestLayout()

        plus.apply {
            background = getDrawable(context, R.drawable.bg_custom_text_view)
            tapCount = 0
            text = "  +"
            visibility = View.INVISIBLE
        }

        addCustomEmoji(plus)
        setupClickListeners(context)

        typedArray.recycle()
    }

    private fun setupClickListeners(context: Context) {
        for (i in 1 until flexboxLayout.childCount) {
            (flexboxLayout.getChildAt(i) as EmojiView).apply {
                background = getDrawable(context, R.drawable.bg_custom_text_view)
                setOnClickListener {
                    it.isSelected = !it.isSelected
                }
            }
        }
    }

    internal fun initStyledAtttributes(typedArray: TypedArray) {
        messageText = typedArray.getString(R.styleable.EmojiMessageView_message) ?: messageText
        name = typedArray.getString(R.styleable.EmojiMessageView_message) ?: name
        image = typedArray.getInteger(R.styleable.EmojiMessageView_image, image)
    }

    fun addNewEmoji(setupText: String) {
        val view = EmojiView(context)
        view.apply {
            text = setupText
            if (setupText.isNotEmpty()) {
                background = getDrawable(context, R.drawable.bg_custom_text_view)
                tapCount = 1
                setOnClickListener {
                    it.isSelected = !it.isSelected
                }
                addCustomEmoji(this)
            }
        }
        if (plus.visibility == View.INVISIBLE) plus.visibility = View.VISIBLE
        requestLayout()
    }

    private fun addCustomEmoji(view: EmojiView) {
        flexboxLayout.addView(view, 0)
        flexboxLayout.requestLayout()
    }

    private fun setupPlusClickListener() {

        plus.apply {
            background = getDrawable(context, R.drawable.bg_custom_text_view)
            tapCount = 0
            setOnClickListener {
                it.isSelected = !it.isSelected
                showAlertDialog()
                it.isSelected = !it.isSelected
            }
        }
    }


    private fun setupValues() {
        avatar.setImageDrawable(getDrawable(context, image))
        messageView.message.text = messageText
        messageView.name.text = name
        setupPlusClickListener()

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

        val flex = if (plus.visibility == View.INVISIBLE) 90 else flexboxLayout.height()

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(
                maxOf(avatar.height(), msgHeight + flex) - 60,
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
                messageView.bottom - 90,
                r - 150
            )
        )
        setupValues()
    }


    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)


}