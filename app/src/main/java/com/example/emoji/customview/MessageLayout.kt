package com.example.emoji.customview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.emoji.R
import com.example.emoji.support.rect
import com.example.emoji.support.height
import com.example.emoji.support.layout
import com.example.emoji.support.width

class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes) {

    private val name: TextView
    private val message: TextView
    private val nameRect = Rect()
    private val messageRect = Rect()

    init {
        LayoutInflater.from(context).inflate(R.layout.message_view_group, this, true)
        name = findViewById(R.id.tv_name)
        name.setTextColor(resources.getColor(R.color.name_color))
        message = findViewById(R.id.tv_message)
        message.setTextColor(resources.getColor(R.color.white))
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val totalWidth = MeasureSpec.getSize(widthMeasureSpec)

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
                message.height() + name.height() + 50,
                maxOf(message.width(), name.width())
            )
        )
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

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)


}