package com.xiangaoole.android.wanandroid.ui.wechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.databinding.ListItemWechatBinding
import com.xiangaoole.android.wanandroid.model.Wechat
import com.xiangaoole.android.wanandroid.ui.ArticleActivity
import timber.log.Timber

class WechatListAdapter : PagingDataAdapter<Wechat, WechatViewHolder>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Wechat>() {
            override fun areItemsTheSame(oldItem: Wechat, newItem: Wechat): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Wechat, newItem: Wechat): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: WechatViewHolder, position: Int) {
        val wechat = getItem(position)
        holder.bind(wechat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WechatViewHolder {
        return WechatViewHolder.create(parent)
    }
}

class WechatViewHolder private constructor(
    private val binding: ListItemWechatBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.cardView.setOnClickListener(::onClick)
    }

    fun bind(wechat: Wechat?) {
        if (wechat != null) {
            binding.wechat = wechat
            binding.executePendingBindings()
        }
    }

    private fun onClick(view: View) {
        binding.wechat?.let {
            ArticleActivity.start(
                view.context,
                id = it.id,
                title = it.title,
                url = it.link
            )
        } ?: Timber.w("onClick when wechat is null")
    }

    companion object {
        fun create(parent: ViewGroup): WechatViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemWechatBinding.inflate(layoutInflater, parent, false)
            return WechatViewHolder(binding)
        }
    }
}