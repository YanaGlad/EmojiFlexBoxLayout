package com.example.emoji.fragments.delegateItem

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DelegateAdapterItemCallback : DiffUtil.ItemCallback<DelegateItem>() {
    override fun areItemsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean {
        val v = oldItem::class
        val v2 = newItem::class
        val ch1 = oldItem.id()
        val ch2 = newItem.id()

        return oldItem::class == newItem::class && oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean {
       val oldHash = oldItem.hashCode()
       val newHash = newItem.hashCode()

        return oldItem.hashCode() == newItem.hashCode()
    }
}