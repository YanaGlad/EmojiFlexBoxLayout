package com.example.emoji.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.emoji.R
import com.example.emoji.support.totalHeight
import com.example.emoji.support.easyLayout
import com.example.emoji.support.easyRect
import com.example.emoji.support.totalWidth
import kotlin.math.max


class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes), View.OnTouchListener {

    val name: TextView
    private var message: TextView
    private val nameRect = Rect()
    private val messageRect = Rect()

    var isMy = false
        set(value) {
            background = if (value)
                getDrawable(context, R.drawable.my_message_gradient)
            else
                getDrawable(context, R.drawable.bg_custom_text_view)
            field = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.message_view_group, this, true)
        name = findViewById(R.id.name)
        name.setTextColor(resources.getColor(R.color.color_teal_default))
        message = findViewById(R.id.msg)
        message.setTextColor(resources.getColor(R.color.white))
    }

    fun setUserName(nameTxt: String) {
        name.text = nameTxt
    }

    fun setMessage(messageTxt: String) {
        message.text = messageTxt
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChildWithMargins(
            name,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )
        measureChildWithMargins(
            message,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0
        )

        val totalWidth =
            if (isMy) {
                max(name.totalWidth() + WIDTH_CORRECTION, message.totalWidth() - 50)
            } else {
                max(name.totalWidth() + 25, message.totalWidth() - 130) + MESSAGE_WIDTH_STEP
            }

        val totalHeight =
            if (isMy) (message.totalHeight() + HEIGHT_CORRECTION)
            else (message.totalHeight() + name.totalHeight() + HEIGHT_CORRECTION)

        val width = if (isMy) message.totalWidth() else maxOf(message.totalWidth(), name.totalWidth())

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, width)
        )
    }

    var longPressed = Runnable {
    }

    override fun onTouch(p0: View?, event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        if (!isMy)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    handler.postDelayed(longPressed, DELAY_MILLIS)
                }
                MotionEvent.ACTION_UP -> {
                    handler.removeCallbacks(longPressed)
                }
                else -> {
                }
            }
        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (!isMy) name.easyLayout(name.easyRect(nameRect, NAME_SIDES, NAME_SIDES))
        val rect = if (isMy) message.easyRect(messageRect, MESSAGE_SIDES, MESSAGE_SIDES)
        else message.easyRect(messageRect, NAME_SIDES, name.bottom, r, isMy)

        message.easyLayout(rect)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)


    companion object {
        const val MESSAGE_SIDES = 24
        const val NAME_SIDES = 20
        private const val HEIGHT_CORRECTION = 50
        private const val WIDTH_CORRECTION = 20
        private const val DELAY_MILLIS = 1000L
        private const val MESSAGE_WIDTH_STEP = 10
    }
}
