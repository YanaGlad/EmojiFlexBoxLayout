package com.example.emoji.support

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.example.emoji.fragments.delegateItem.DateDelegateItem
import com.example.emoji.fragments.delegateItem.DelegateItem
import com.example.emoji.fragments.delegateItem.UserDelegateItem
import com.example.emoji.model.DateModel
import com.example.emoji.model.UserModel


fun View.width(): Int {
    val margins = this.layoutParams as ViewGroup.MarginLayoutParams
    return this.measuredWidth + margins.leftMargin + margins.rightMargin
}

fun View.height(): Int {
    val margins = this.layoutParams as ViewGroup.MarginLayoutParams
    return this.measuredHeight + margins.topMargin + margins.bottomMargin
}

fun View.layout(rect: Rect) = layout(rect.left, rect.top, rect.right, rect.bottom)

fun View.rect(rect: Rect, l: Int, t: Int): Rect {
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

fun View.rectLeft(rect: Rect, r: Int, t: Int): Rect {
    val viewLayoutParams: ViewGroup.MarginLayoutParams =
        this.layoutParams as ViewGroup.MarginLayoutParams

    rect.apply {
        left = rect.right - measuredWidth - viewLayoutParams.leftMargin // l + viewLayoutParams.leftMargin
        top = t + viewLayoutParams.topMargin
        right = r - viewLayoutParams.rightMargin//  rect.left + measuredWidth + viewLayoutParams.rightMargin
        bottom = rect.top + measuredHeight + viewLayoutParams.bottomMargin
    }
    return rect
}

fun View.rectLeft(
    rect: Rect,
    leftBorder: Int,
    topBorder: Int,
    rightBorder: Int
): Rect {
    val viewLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams

    viewLayoutParams.also {
        rect.apply {
            left = rightBorder - leftBorder - it.leftMargin
            top = topBorder + it.topMargin
            right = rightBorder + it.rightMargin
            bottom = top + measuredHeight + it.bottomMargin
        }
    }
    return rect
}


fun View.rect(
    rect: Rect,
    leftBorder: Int,
    topBorder: Int,
    rightBorder: Int
): Rect {
    val viewLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams

    viewLayoutParams.also {
        rect.apply {
            left = leftBorder + it.leftMargin
            top = topBorder + it.topMargin
            right = rightBorder + it.rightMargin
            bottom = top + measuredHeight + it.bottomMargin
        }
    }
    return rect
}

fun List<UserModel>.toDelegateItemListWithDate(): List<DelegateItem> {
    val delegateItemList: MutableList<DelegateItem> = mutableListOf()
    var lastDate = ""
    this
        .sortedBy { it.date.toInt() }
        .forEach { userModel ->
            if (userModel.date != lastDate) {
                delegateItemList.add(
                    DateDelegateItem(
                        DateModel(userModel.date, userModel.month)
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


//private fun defaultAlertDialog(reactions: ArrayList<String>, emojiView: EmojiMessageView) {
//    val gridView = GridView(view.context)
//    gridView.apply {
//        adapter = ArrayAdapter(
//            context,
//            R.layout.simple_list_item_1,
//            reactions
//        )
//        numColumns = 5
//    }
//
//    val alert: AlertDialog = AlertDialog.Builder(view.context)
//        .setView(gridView)
//        .create()
//
//    gridView.onItemClickListener =
//        AdapterView.OnItemClickListener { parent, _, position, _ ->
//            emojiView.addNewEmoji((parent.getChildAt(position) as TextView).text.toString())
//            reactions.removeAt(position)
//            alert.hide()
//        }
//
//    if (reactions.isNotEmpty())
//        alert.show()
//    else Toast.makeText(view.context, itemView.context.getString(com.example.emoji.R.string.out_of_emoji), Toast.LENGTH_SHORT)
//        .show()
//}