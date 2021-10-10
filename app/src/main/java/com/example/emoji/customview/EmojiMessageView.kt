package com.example.emoji.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
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
    private val messageView: MessageLayout
    private val flexboxLayout: FlexBoxLayout
    private val avatarRect = Rect()
    private val tvMessageRect = Rect()
    private val flexboxRect = Rect()

    var setupText: MutableLiveData<String> = MutableLiveData<String>()
    var reactions = EmojiFactory().getEmoji()

    var messageText = "Message test"
    var name = "Yana Glad"
    var image: Int = R.drawable.ic_image
    var onPlusClickLister: OnClickListener = OnClickListener { }
    var showAlertDialog: () -> Unit = {}

    val plus = EmojiView(context)

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiMessageView,
            defStyleAttrs,
            defStyleRes
        )

        messageText = typedArray.getString(R.styleable.EmojiMessageView_message) ?: messageText
        name = typedArray.getString(R.styleable.EmojiMessageView_message) ?: name
        image = typedArray.getInteger(R.styleable.EmojiMessageView_image, image)

        LayoutInflater.from(context).inflate(R.layout.emoji_view_group, this, true)

        avatar = findViewById(R.id.avatar)
        messageView = findViewById(R.id.message_title)

        setupValues(context)

        flexboxLayout = findViewById(R.id.flexbox)

        setupText.value = ""


        plus.background = getDrawable(context, R.drawable.bg_custom_text_view)
        plus.tap_count = 0
        plus.text = "  +"

        plus.setOnClickListener(onPlusClickLister)

        flexboxLayout.addView(plus)
        flexboxLayout.requestLayout()

        setupText.observe((context as LifecycleOwner), {
            addCustomEmoji(context)
        })

        setupPlusClickListener(context)

        for (i in 1 until flexboxLayout.childCount) {
            (flexboxLayout.getChildAt(i) as EmojiView).apply {
                background = getDrawable(context, R.drawable.bg_custom_text_view)
                setOnClickListener {
                    it.isSelected = !it.isSelected
                }
            }
        }
    }

    private fun addCustomEmoji(context: Context) {
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
    }

    private fun setupPlusClickListener(context: Context) {
        (flexboxLayout.getChildAt(0) as EmojiView).apply {
            background = getDrawable(context, R.drawable.bg_custom_text_view)
            tap_count = 0
            setOnClickListener {
                it.isSelected = !it.isSelected
                if (reactions.isNotEmpty())
                    showAlertDialog()
                it.isSelected = !it.isSelected
            }
        }
    }

    private fun setupValues(context: Context) {
        avatar.setImageDrawable(getDrawable(context, image))
        messageView.message.text = messageText
        messageView.name.text = name
        plus.setOnClickListener(onPlusClickLister)
        if (flexboxLayout != null)
            setupPlusClickListener(context)

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

        setupValues(context)
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)


}