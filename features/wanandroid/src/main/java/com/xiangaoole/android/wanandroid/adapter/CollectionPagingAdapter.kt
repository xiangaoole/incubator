package com.xiangaoole.android.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.ListItemHomeBinding
import com.xiangaoole.android.wanandroid.model.CollectionArticle
import com.xiangaoole.android.wanandroid.ui.ArticleActivity
import com.xiangaoole.android.wanandroid.util.bindUrl
import com.xiangaoole.android.wanandroid.util.htmlText
import timber.log.Timber

class CollectionPagingAdapter :
    BasePagingAdapter<CollectionArticle, CollectionViewHolder>({ a, b -> a.id == b.id }) {
    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder.create(parent)
    }
}

class CollectionViewHolder private constructor(
    private val binding: ListItemHomeBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var article: CollectionArticle? = null

    init {
        binding.cardView.setOnClickListener(::onClick)
    }

    fun bind(article: CollectionArticle?) {
        this.article = article
        article?.run {
            binding.tvTopTip.isVisible = top
            binding.tvNewTip.isVisible = fresh
            binding.tvArticleTag.isVisible = tags.isNotEmpty()
            if (tags.isNotEmpty()) {
                binding.tvArticleTag.text = tags[0].name
            }
            binding.titleText.htmlText(title)
            binding.shareDateText.text = niceDate
            binding.authorText.text = when {
                author.isNotEmpty() -> author
                shareUser.isNotEmpty() -> shareUser
                else -> binding.root.resources.getString(R.string.default_author)
            }
            binding.chapterName.text = when {
                chapterName.isNotEmpty() && superChapterName.isNotEmpty() ->
                    "$superChapterName / $chapterName"
                chapterName.isNotEmpty() -> chapterName
                superChapterName.isNotEmpty() -> superChapterName
                else -> ""
            }
            binding.ivThumbnail.isVisible = envelopePic.isNotEmpty()
            if (envelopePic.isNotEmpty()) {
                binding.ivThumbnail.bindUrl(envelopePic)
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
        fun create(parent: ViewGroup): CollectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemHomeBinding.inflate(layoutInflater, parent, false)
            return CollectionViewHolder(binding)
        }
    }
}