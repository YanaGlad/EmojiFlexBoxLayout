package com.example.emoji.support

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup


fun View.width(): Int {
    val margins = this.layoutParams as ViewGroup.MarginLayoutParams
    return this.measuredWidth + margins.leftMargin + margins.rightMargin
}

fun View.height(): Int {
    val margins = this.layoutParams as ViewGroup.MarginLayoutParams
    return this.measuredHeight + margins.topMargin + margins.bottomMargin
}

fun View.layout(rect: Rect) = layout(rect.left, rect.top, rect.right, rect.bottom)

fun View.rect(rect: Rect, _left: Int, _top: Int): Rect {
    val viewLayoutParams: ViewGroup.MarginLayoutParams =
        this.layoutParams as ViewGroup.MarginLayoutParams

    rect.apply {
        left = _left + viewLayoutParams.leftMargin
        top = _top + viewLayoutParams.topMargin
        right = rect.left + measuredWidth + viewLayoutParams.rightMargin
        bottom = rect.top + measuredHeight + viewLayoutParams.bottomMargin
    }
    return rect
}

fun View.rect(
    rect: Rect,
    _left: Int,
    _top: Int,
    rightBorder: Int
): Rect {
    val viewLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams

    viewLayoutParams.also {
        rect.apply {
            left = _left + it.leftMargin
            top = _top + it.topMargin
            right = rightBorder + it.rightMargin
            bottom = top + measuredHeight + it.bottomMargin
        }
    }

    return rect
}