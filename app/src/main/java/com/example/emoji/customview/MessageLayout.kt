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
import com.example.emoji.support.height
import com.example.emoji.support.layout
import com.example.emoji.support.rect
import com.example.emoji.support.width
import kotlin.math.max


class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes), View.OnTouchListener {

    val name: TextView
    private var message: TextView
    private val nameRect = Rect()
    private val messageRect = Rect()
    private val heightCorrection = 50
    private val widthCorrection = 10

    var isMy = false
    set(value) {
        background = if(value)
            getDrawable(context, R.drawable.my_message_gradient)
        else
            getDrawable(context, R.drawable.bg_custom_text_view)
        field = value
    }

    private val myWidthStep = 45
    private val otherWidthStep = 10


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
            if (isMy) message.width() - myWidthStep else max(name.width(), message.width()) + otherWidthStep

        val totalHeight =
            if (isMy) (message.height() + heightCorrection) else (message.height() + name.height() + heightCorrection)

        val width = if (isMy) message.width() else maxOf(message.width(), name.width() - widthCorrection)
        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(
                totalHeight,
                width
            )
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
                    handler.postDelayed(longPressed, 1000)
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
        if (!isMy) name.layout(name.rect(nameRect, 20, 20))
        val rect = if (isMy) message.rect(messageRect, 24, 24) else message.rect(messageRect, 20, name.bottom, r)
        message.layout(
            rect
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)


}