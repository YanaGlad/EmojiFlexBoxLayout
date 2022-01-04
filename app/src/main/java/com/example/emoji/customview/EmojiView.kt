package com.example.emoji.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.example.emoji.R


class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes), View.OnClickListener {

    var emojiClick: () -> Unit = { }
    var emojiClicked: () -> Unit = { }
    var emojiNotClicked: () -> Unit = { }

    var text = ""

    var tapCount: Int = 0
        set(value) {
            field = value
            requestLayout()
        }

    private var minTextLength = "\uD83E\uDD70"
    private var customSize = 0f
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = DEFAULT_SIZE
        textAlign = Paint.Align.CENTER
    }

    private val textBounds = Rect()
    private val textCoordinate = PointF()
    private val tempFontMetrics = Paint.FontMetrics()

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

        tapCount = typedArray.getInteger(R.styleable.EmojiView_tap_count, 0)
        customSize = typedArray.getDimension(R.styleable.EmojiView_textSize, DEFAULT_SIZE)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textPaint.getTextBounds(
            (minTextLength + tapCount.toString()),
            0,
            (minTextLength + tapCount.toString()).length,
            textBounds
        )

        val textHeight = textBounds.height() + HEIGHT_STEP
        val textWidth = textBounds.width() + WIDTH_STEP

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
            textCoordinate.x - TEXT_MARGIN * tapCount.toString().length,
            textCoordinate.y,
            textPaint
        )
        textPaint.textSize = customSize
        if (tapCount > 0)
            canvas.drawText(
                tapCount.toString(),
                textCoordinate.x + STEP_FORMAT_TEXT,
                textCoordinate.y,
                textPaint
            )
        textPaint.textSize = DEFAULT_SIZE
    }

    override fun onClick(p0: View?) {
        isSelected = !isSelected
    }

    var checked = false

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
            if (tapCount != 0 && !checked) {
                tapCount++
                checked = true
                emojiClicked()
                requestLayout()
            }
        } else {
            if (tapCount != 0 && checked) {
                tapCount--
                checked = false
                emojiNotClicked()
                requestLayout()
            }
        }
        return drawableState
    }

    companion object {
        private const val HEIGHT_STEP = 50
        private const val WIDTH_STEP = 80
        private const val STEP_FORMAT_TEXT = 30
        private const val DEFAULT_SIZE = 42f
        private const val TEXT_MARGIN = 15
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
    }
}


//companion object {
//    private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
//}

