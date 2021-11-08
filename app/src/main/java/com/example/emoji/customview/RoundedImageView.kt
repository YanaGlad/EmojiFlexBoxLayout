package com.example.emoji.customview


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.emoji.R


class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttrs) {

    init {
        clipToOutline = true
        setBackgroundResource(R.drawable.circle)
        scaleType = ScaleType.CENTER_CROP
    }
}
