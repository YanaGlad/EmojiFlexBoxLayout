package com.example.emoji.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.*
import android.widget.TextView
import com.example.emoji.R
import com.example.emoji.support.height
import com.example.emoji.support.layout
import com.example.emoji.support.rect
import com.example.emoji.support.width
import kotlin.math.max

import android.widget.Toast
import android.view.MotionEvent


class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes), View.OnTouchListener {

    val name: TextView
    var message: TextView
    private val nameRect = Rect()
    private val messageRect = Rect()
    private val heightCorrection = 50
    private val widthCorrection = 10

    init {
        LayoutInflater.from(context).inflate(R.layout.message_view_group, this, true)
        name = findViewById(R.id.name)
        name.setTextColor(resources.getColor(R.color.color_teal_default))
        message = findViewById(R.id.msg)
        message.setTextColor(resources.getColor(R.color.white))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val totalWidth = max(name.width(), message.width()) + 10

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
        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(
                message.height() + name.height() + heightCorrection,
                maxOf(message.width(), name.width() - widthCorrection)
            )
        )
    }

    public var longPressed = Runnable {
        Toast.makeText(context, "Long detected!", Toast.LENGTH_SHORT).show()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) handler.postDelayed(
            longPressed,
            1000
        ) else {
            handler.removeCallbacks(longPressed)
        }
        return super.onTouchEvent(event)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        name.layout(name.rect(nameRect, 20, 20))

        message.layout(
            message.rect(
                messageRect,
                20,
                name.bottom,
                r
            )
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun onTouch(p0: View?, event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }
}