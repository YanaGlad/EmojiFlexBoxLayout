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
        set(value) {
            field = value
            requestLayout()
        }

    var tap_count: Int = 0
    private var customSize = 0f
    private var defaultSize = 42f
    private val defaultEmoji = "\uD83E\uDD70"

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = defaultSize
        textAlign = Paint.Align.CENTER
    }

    private val textBounds = Rect()
    private val textCoordinate = PointF()
    private var checked = false
    private val tempFontMetrics = Paint.FontMetrics()

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EmojiView,
            defStyleAttr,
            defStyleRes
        )

        text = typedArray.getString(R.styleable.EmojiView_customText).orEmpty()
        textPaint.color =
            typedArray.getColor(R.styleable.EmojiView_customTextColor, Color.WHITE)

        tap_count = typedArray.getInteger(R.styleable.EmojiView_tap_count, 1)

        val sizeTODO = typedArray.getDimension(R.styleable.EmojiView_textSize, 60f)
        customSize = defaultSize - 10

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(
            (defaultEmoji + tap_count.toString()),
            0,
            (defaultEmoji + tap_count.toString()).length,
            textBounds
        )

        val textHeight = textBounds.height() + 30
        val textWidth = textBounds.width() + 80

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


    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
            if (tap_count != 0 && !checked) {
                tap_count++
                checked = true
                requestLayout()
            }
        } else {
            if (tap_count != 0 && checked) {
                tap_count--
                checked = false
                requestLayout()
            }
        }
        return drawableState
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawText(
            text,
            textCoordinate.x - 15 * tap_count.toString().length,
            textCoordinate.y,
            textPaint
        )
        textPaint.textSize = customSize
        if (tap_count > 0)
            canvas.drawText(
                tap_count.toString(),
                textCoordinate.x + 30,
                textCoordinate.y,
                textPaint
            )
        textPaint.textSize = defaultSize
    }

    companion object {
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }

    override fun onClick(p0: View?) {
        isSelected = !isSelected
    }
}