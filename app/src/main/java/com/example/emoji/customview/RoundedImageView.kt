package com.example.emoji.customview


import android.content.Context
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import com.example.emoji.R


class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttrs) {

    init {
        outlineProvider = ViewOutlineProvider.BACKGROUND
        clipToOutline = true
        setBackgroundResource(R.drawable.circle)
        scaleType = ScaleType.CENTER_CROP
    }
}