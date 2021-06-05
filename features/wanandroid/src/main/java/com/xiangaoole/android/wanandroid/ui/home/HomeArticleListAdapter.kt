package com.xiangaoole.android.wanandroid.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.databinding.ListItemHomeBinding
import com.xiangaoole.android.wanandroid.model.HomeArticle
import com.xiangaoole.android.wanandroid.ui.ArticleActivity
import com.xiangaoole.android.wanandroid.util.htmlText
import timber.log.Timber

class HomeArticleListAdapter : PagingDataAdapter<HomeArticle, HomeArticleViewHolder>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<HomeArticle>() {
            override fun areItemsTheSame(oldItem: HomeArticle, newItem: HomeArticle): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HomeArticle, newItem: HomeArticle): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: HomeArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeArticleViewHolder {
        return HomeArticleViewHolder.create(parent)
    }
}

class HomeArticleViewHolder private constructor(
    private val binding: ListItemHomeBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var article: HomeArticle? = null

    init {
        binding.cardView.setOnClickListener(::onClick)
    }

    fun bind(article: HomeArticle?) {
        this.article = article
        article?.run {
            binding.tvTopTip.isVisible = top
            binding.tvNewTip.isVisible = fresh
            binding.tvArticleTag.isVisible = tags.isNotEmpty()
            if (tags.isNotEmpty()) {
                binding.tvArticleTag.text = tags[0].name
            }
            binding.titleText.htmlText(article.title)
            binding.shareDateText.text = niceDate
            binding.authorText.text = if (author.isNotEmpty()) author else shareUser
            binding.chapterName.text = when {
                chapterName.isNotEmpty() && superChapterName.isNotEmpty() ->
                    "$superChapterName / $chapterName"
                chapterName.isNotEmpty() -> chapterName
                superChapterName.isNotEmpty() -> superChapterName
                else -> ""
            }
        }
    }

    private fun onClick(view: View) {
        article?.let {
            ArticleActivity.start(
                view.context,
                id = it.id,
                title = it.title,
                url = it.link
            )
        } ?: Timber.w("onClick when article is null")
    }

    companion object {
        fun create(parent: ViewGroup): HomeArticleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemHomeBinding.inflate(layoutInflater, parent, false)
            return HomeArticleViewHolder(binding)
        }
    }
}