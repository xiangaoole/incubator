package com.xiangaoole.android.wanandroid.ui.wechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.databinding.ListItemWechatBinding
import com.xiangaoole.android.wanandroid.model.Project
import com.xiangaoole.android.wanandroid.ui.article.ArticleActivity
import timber.log.Timber

class WechatListAdapter : PagingDataAdapter<Project, WechatViewHolder>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: WechatViewHolder, position: Int) {
        val project = getItem(position)
        holder.bind(project)
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

    fun bind(project: Project?) {
        if (project != null) {
            binding.project = project
            binding.executePendingBindings()
        }
    }

    private fun onClick(view: View) {
        binding.project?.let {
            ArticleActivity.start(
                view.context,
                id = it.id,
                title = it.title,
                url = it.link
            )
        } ?: Timber.w("onClick when project is null")
    }

    companion object {
        fun create(parent: ViewGroup): WechatViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemWechatBinding.inflate(layoutInflater, parent, false)
            return WechatViewHolder(binding)
        }
    }
}