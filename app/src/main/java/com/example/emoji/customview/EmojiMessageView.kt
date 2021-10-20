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
import com.example.emoji.support.*

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

   var isMy = false

    fun setIsMy(value: Boolean) {
        messageView.isMy = value
        isMy = value
        if(value)
            messageView.name.visibility = View.GONE
        else messageView.name.visibility = View.VISIBLE
    }

    val plus = EmojiView(context)

    private val leftRightCorrection = 150
    private val topCorrection = 90
    var showAlertDialog: () -> Unit = {}

    var messsageText = ""
    var nameText = "Yana Glad"

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

        initStyledAtttributes(typedArray)

        plus.apply {
            background = getDrawable(context, R.drawable.bg_custom_text_view)
            tapCount = 0
            text = "  +"
            visibility = View.INVISIBLE
        }

        setupPlusClickListener()
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
        setMessage(typedArray.getString(R.styleable.EmojiMessageView_message) ?: messsageText)
        setUserName(typedArray.getString(R.styleable.EmojiMessageView_message) ?: nameText)
        setAvatar(typedArray.getInteger(R.styleable.EmojiMessageView_image, R.drawable.ic_launcher_background))
        isMy = typedArray.getBoolean(R.styleable.EmojiMessageView_isMe, false)
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

    fun setMessage(text: String) {
        messsageText = text
        messageView.setMessage(messsageText)
        requestLayout()
    }

    fun setUserName(nameText: String) {
        this.nameText = nameText
        messageView.setUserName(nameText)
        requestLayout()
    }

    fun setAvatar(image: Int) {
        avatar.setImageDrawable(getDrawable(context, image))
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildWithMargins(avatar, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(messageView, widthMeasureSpec, 0, heightMeasureSpec, 0)
  //      if (isMy) flexboxLayout.reversed = true

        val msgHeight = messageView.height()
        measureChildWithMargins(
            flexboxLayout,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        val flex = if (plus.visibility == View.INVISIBLE) topCorrection else flexboxLayout.height()

        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(
                 msgHeight + flex - 60,
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
            avatar.layout(avatar.rect(avatarRect, 0, 0))
            messageView.layout(messageView.rect(tvMessageRect, avatar.right, 0))

            layoutFlexBox(20, messageView.bottom - topCorrection, r - leftRightCorrection)
    //    }
    }

    private fun layoutFlexBox(leftBorder: Int, rightBorder: Int, topBorder: Int) {
        flexboxLayout.layout(
            flexboxLayout.rect(
                flexboxRect,
                leftBorder,
                rightBorder,
                topBorder - 60,
            )
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)


}