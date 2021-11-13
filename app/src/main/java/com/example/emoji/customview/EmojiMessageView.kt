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
import com.example.emoji.support.easyLayout
import com.example.emoji.support.easyRect
import com.example.emoji.support.totalHeight

class EmojiMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes) {

    val avatar: RoundedImageView
    val messageView: MessageLayout
    private val flexboxLayout: FlexBoxLayout
    private val avatarRect = Rect()
    private val tvMessageRect = Rect()
    private val flexboxRect = Rect()

    private var isMy = false

    fun setIsMy(value: Boolean) {
        messageView.isMy = value
        isMy = value
        if (value)
            messageView.name.visibility = View.GONE
        else messageView.name.visibility = View.VISIBLE
    }

    val plus = EmojiView(context)

    companion object {
        private const val LEFT_RIGHT_CORRECTION = 150
        private const val TOP_CORRECTION = 90
        private const val FLEX_MARGIN = 60
        private const val FLEX_LEFT_BOARDER = 20
    }

    private var messageText = ""
    private var nameText = "Yana Glad"

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiMessageView,
            defStyleAttrs,
            defStyleRes
        )

        LayoutInflater.from(context).inflate(R.layout.emoji_view_group, this, true)
        avatar = findViewById(R.id.avatar)
        messageView = findViewById(R.id.message_title)
        flexboxLayout = findViewById(R.id.flexbox)
        messageView.requestLayout()

        initStyledAttributes(typedArray)

        initPlus(context)

        addCustomEmoji(plus)
        setupClickListeners(context)

        typedArray.recycle()
    }

    fun initPlus(context: Context) {
        plus.apply {
            background = getDrawable(context, R.drawable.bg_custom_text_view)
            tapCount = 0
            text = "  +"
            visibility = INVISIBLE
        }
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

    private fun initStyledAttributes(typedArray: TypedArray) {
        setMessage(typedArray.getString(R.styleable.EmojiMessageView_message) ?: messageText)
        setUserName(typedArray.getString(R.styleable.EmojiMessageView_message) ?: nameText)
        setAvatar(typedArray.getInteger(R.styleable.EmojiMessageView_image, R.drawable.ic_launcher_background))
        isMy = typedArray.getBoolean(R.styleable.EmojiMessageView_isMe, false)
    }

    fun addNewEmoji(count : Int, setupText: String, clicked : Boolean, onEmojiClick: () -> Unit ) {
        val view = EmojiView(context)
        view.apply {
            text = setupText
            if (setupText.isNotEmpty()) {
                background = getDrawable(context, R.drawable.bg_custom_text_view)
                tapCount = count
                setOnClickListener {
                    it.isSelected = !it.isSelected
                }
                emojiClick = onEmojiClick
                isSelected = clicked
                addCustomEmoji(this)
            }
        }
        if (plus.visibility == View.INVISIBLE) plus.visibility = View.VISIBLE
        requestLayout()
    }

    fun clearFlexBox() {
        flexboxLayout.removeAllViews()
    }


    fun addCustomEmoji(view: EmojiView) {
        flexboxLayout.also {
            it.addView(view, 0)
            it.requestLayout()
        }
    }

    fun setupPlusClickListener(showAlertDialog: () -> Unit = {}) {
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

    fun setMessage(text: String) {
        messageText = text
        messageView.setMessage(messageText)
        requestLayout()
    }

    fun setUserName(nameText: String) {
        this.nameText = nameText
        messageView.setUserName(nameText)
        requestLayout()
    }

    private fun setAvatar(image: Int) {
        avatar.setImageDrawable(getDrawable(context, image))
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildWithMargins(avatar, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(messageView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        //      if (isMy) flexboxLayout.reversed = true

        val msgHeight = messageView.totalHeight()
        measureChildWithMargins(
            flexboxLayout,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        val flex = if (plus.visibility == View.INVISIBLE) TOP_CORRECTION else flexboxLayout.totalHeight()

        val totalWidth = MeasureSpec.getSize(widthMeasureSpec) - 20

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(
                msgHeight + flex - FLEX_MARGIN,
                heightMeasureSpec
            )
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        if (isMy) {
//            //avatar.layout(avatar.rectLeft(avatarRect, r, 0))
//            messageView.layout(messageView.rectLeft(tvMessageRect, r , 0))
//
//            layoutFlexBox(
//                this.left + messageView.width() - leftRightCorrection,
//                r - leftRightCorrection,
//                messageView.bottom - topCorrection
//            )
//
//        } else {
        avatar.easyLayout(avatar.easyRect(avatarRect, 0, 0))
        messageView.easyLayout(messageView.easyRect(tvMessageRect, avatar.right, 0))

        layoutFlexBox(FLEX_LEFT_BOARDER, messageView.bottom - TOP_CORRECTION, r - LEFT_RIGHT_CORRECTION)
    }

    private fun layoutFlexBox(leftBorder: Int, rightBorder: Int, topBorder: Int) {
        flexboxLayout.easyLayout(
            flexboxLayout.easyRect(
                flexboxRect,
                leftBorder,
                rightBorder,
                topBorder - FLEX_MARGIN,
            )
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)
}
