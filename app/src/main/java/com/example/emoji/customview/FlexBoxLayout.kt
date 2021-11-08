package com.example.emoji.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.getMode
import android.view.View.MeasureSpec.getSize
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.AT_MOST
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import com.example.emoji.R
import kotlin.math.max


class FlexBoxLayout(context: Context, attributeSet: AttributeSet) :
    ViewGroup(context, attributeSet) {
    private var lineHeight: Int = 0
    private var countSpaces: Int = 0

    private var columnCount = -1
    var reversed = false

    init {
        context.withStyledAttributes(attributeSet, R.styleable.MyFlowLayout) {
            columnCount = getInteger(R.styleable.MyFlowLayout_column_count, -1)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var height = getSize(heightMeasureSpec) - paddingTop - paddingBottom

        var x = paddingLeft
        var y = paddingTop


        val childHeightMeasureSpec = makeMeasureSpec(
            height, if (getMode(heightMeasureSpec) == AT_MOST) AT_MOST else UNSPECIFIED
        )

        children.forEach { child ->
            if (child.visibility != GONE) {
                val layoutParams = child.layoutParams as LayoutParamsWithSpacing
                child.measure(makeMeasureSpec(width, AT_MOST), childHeightMeasureSpec)
                val childWidth = child.measuredWidth
                lineHeight = max(lineHeight, child.measuredHeight + layoutParams.verticalSpacing)


                x += childWidth + layoutParams.horizontalSpacing

                if (x > width) {
                    x = paddingLeft
                    y += lineHeight
                    countSpaces++
                    x += childWidth + layoutParams.horizontalSpacing
                }
            }
        }

        if (getMode(heightMeasureSpec) == UNSPECIFIED ||
            getMode(heightMeasureSpec) == AT_MOST && y + lineHeight < height || x > width
        ) height = y

        setMeasuredDimension(width, height + lineHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        var x = paddingLeft
        var y = paddingTop// + lineHeight

        var count = 0
        children.forEach {
            with(it) {
                if (visibility != View.GONE) {
                    val layoutParams = layoutParams as LayoutParamsWithSpacing
                    val childWidth = measuredWidth

                    if (x + childWidth > width || count == columnCount) {
                        x = paddingLeft
                        y += lineHeight
                        count = 0
                    }
                    count++

                    layout(
                        x, y, x + childWidth,
                        y + measuredHeight // + lineHeight
                    )

                    x += layoutParams.horizontalSpacing
                    x += childWidth
                }
            }
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        LayoutParamsWithSpacing(HORIZONTAL_SPACING, VERTICAL_SPACING)

    override fun checkLayoutParams(layoutParams: LayoutParams) =
        layoutParams is LayoutParamsWithSpacing

    class LayoutParamsWithSpacing(val horizontalSpacing: Int, val verticalSpacing: Int) :
        LayoutParams(0, 0)

    companion object{
        const val HORIZONTAL_SPACING = 15
        const val VERTICAL_SPACING = 20

    }
}
