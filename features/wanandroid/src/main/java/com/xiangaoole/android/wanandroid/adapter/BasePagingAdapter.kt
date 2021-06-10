package com.xiangaoole.android.wanandroid.adapter

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class BasePagingAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    isSame: (T, T) -> Boolean
) : PagingDataAdapter<T, VH>(getDiff(isSame)) {

    companion object {
        fun <T : Any> getDiff(isSame: (T, T) -> Boolean) = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return isSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return Objects.equals(oldItem, newItem)
            }

        }
    }
}