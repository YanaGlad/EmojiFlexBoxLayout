package com.example.emoji.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.emoji.R


class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes), View.OnClickListener {

    var text = ""

    var tapCount: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    private var customSize = 0f
    private val defaultSize = 42f
    private var minTextLength = "\uD83E\uDD70"

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = defaultSize
        textAlign = Paint.Align.CENTER
    }

    private val textBounds = Rect()
    private val textCoordinate = PointF()
    private val tempFontMetrics = Paint.FontMetrics()
    private val heightStep = 50
    private val widthStep = 80
    private val stepTextFromStart = 30

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiView,
            defStyleAttr,
            defStyleRes
        )

        text = typedArray.getString(R.styleable.EmojiView_customText).orEmpty()
        if (text.length > minTextLength.length) minTextLength = text

        textPaint.color =
            typedArray.getColor(R.styleable.EmojiView_customTextColor, Color.WHITE)

        tapCount = typedArray.getInteger(R.styleable.EmojiView_tap_count, 1)
        customSize = typedArray.getDimension(R.styleable.EmojiView_textSize, defaultSize)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(
            (minTextLength + tapCount.toString()),
            0,
            (minTextLength + tapCount.toString()).length,
            textBounds
        )

        val textHeight = textBounds.height() + heightStep
        val textWidth = textBounds.width() + widthStep

        val totalWidth = textWidth + paddingRight + paddingLeft
        val totalHeight = textHeight + paddingTop + paddingBottom

        val resultWidth = resolveSize(totalWidth, widthMeasureSpec)
        val resultHeight = resolveSize(totalHeight, heightMeasureSpec)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        textPaint.getFontMetrics(tempFontMetrics)
        textCoordinate.x = w / 2f
        textCoordinate.y = h / 2f + textBounds.height() / 2 - tempFontMetrics.descent
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(
            text,
            textCoordinate.x - 15 * tapCount.toString().length,
            textCoordinate.y,
            textPaint
        )
        textPaint.textSize = customSize
        if (tapCount > 0)
            canvas.drawText(
                tapCount.toString(),
                textCoordinate.x + stepTextFromStart,
                textCoordinate.y,
                textPaint
            )
        textPaint.textSize = defaultSize
    }



    override fun onClick(p0: View?) {
        isSelected = !isSelected
    }
}



//    override fun onCreateDrawableState(extraSpace: Int): IntArray {
//        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
//        if (isSelected) {
//            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
//            if (tap_count != 0 && !checked) {
//                tap_count++
//                checked = true
//                requestLayout()
//            }
//        } else {
//            if (tap_count != 0 && checked) {
//                tap_count--
//                checked = false
//                requestLayout()
//            }
//        }
//        return drawableState
//    }
//companion object {
//    private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
//}

