package com.example.emoji.support

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.emoji.fragments.delegateItem.DelegateItem
import com.example.emoji.fragments.delegateItem.UserDelegateItem
import com.example.emoji.fragments.delegateItem.StreamDelegateItem
import com.example.emoji.fragments.delegateItem.TopicDelegateItem
import com.example.emoji.fragments.delegateItem.DateDelegateItem
import com.example.emoji.model.DateModel
import com.example.emoji.model.PutValueToMonth
import com.example.emoji.model.MessageModel
import com.example.emoji.model.StreamModel
import java.util.*


fun loadImage(context: Context, url: String?, view: ImageView) {
    Glide.with(context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(view)
}

fun View.totalWidth(): Int {
    val margins = this.layoutParams as ViewGroup.MarginLayoutParams
    return this.measuredWidth + margins.leftMargin + margins.rightMargin
}

fun View.totalHeight(): Int {
    val margins = this.layoutParams as ViewGroup.MarginLayoutParams
    return this.measuredHeight + margins.topMargin + margins.bottomMargin
}

fun View.easyLayout(rect: Rect) = layout(rect.left, rect.top, rect.right, rect.bottom)

fun View.easyRect(rect: Rect, l: Int, t: Int): Rect {
    val viewLayoutParams: ViewGroup.MarginLayoutParams =
        this.layoutParams as ViewGroup.MarginLayoutParams

    rect.apply {
        left = l + viewLayoutParams.leftMargin
        top = t + viewLayoutParams.topMargin
        right = rect.left + measuredWidth + viewLayoutParams.rightMargin
        bottom = rect.top + measuredHeight + viewLayoutParams.bottomMargin
    }
    return rect
}

fun View.easyRect(
    rect: Rect,
    leftBorder: Int,
    topBorder: Int,
    rightBorder: Int,
    isMy: Boolean = false,
): Rect {
    val viewLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams

    viewLayoutParams.also {
        rect.apply {
            left = if (!isMy) leftBorder + it.leftMargin else leftBorder + it.leftMargin + 25
            top = topBorder + it.topMargin
            right = rightBorder + it.rightMargin
            bottom = top + measuredHeight + it.bottomMargin
        }
    }
    return rect
}

val valueToInt = PutValueToMonth()
fun List<MessageModel>.toDelegateItemListWithDate(): List<DelegateItem> {
    val delegateItemList: MutableList<DelegateItem> = mutableListOf()
    var lastDate = ""

    this
        .sortedBy { it.date.toInt() + valueToInt.getValueByMonth(it.month.lowercase(Locale.getDefault())) }
        .forEach { userModel ->
            if (userModel.date != lastDate) {
                val model = DateModel(userModel.date, userModel.month)
                delegateItemList.add(
                    DateDelegateItem(
                        model.dateNumber.hashCode(), //TODO заменю реальными id, когда будут данные с бэка
                        model
                    )
                )
                delegateItemList.add(
                    UserDelegateItem(
                        userModel
                    )
                )
                lastDate = userModel.date
            } else {
                delegateItemList.add(
                    UserDelegateItem(
                        userModel
                    )
                )
            }
        }
    return delegateItemList
}


fun List<StreamModel>.toDelegateStreamsItemList(pos: Int): List<DelegateItem> {
    val delegateItemList: MutableList<DelegateItem> = mutableListOf()
    var count = 0
    this
        .forEach { streamModel ->
            streamModel.clicked = pos == count
            delegateItemList.add(
                StreamDelegateItem(
                    streamModel
                )
            )
            if (pos == count) {
                streamModel.topics.forEach {
                    delegateItemList.add(
                        TopicDelegateItem(
                            it.hashCode(),
                            it
                        )
                    )
                }
            }
            count++
        }
    return delegateItemList
}
