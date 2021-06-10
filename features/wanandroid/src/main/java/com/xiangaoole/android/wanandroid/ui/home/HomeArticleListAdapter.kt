package com.xiangaoole.android.wanandroid.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.BannerHomeBinding
import com.xiangaoole.android.wanandroid.databinding.ListItemHomeBinding
import com.xiangaoole.android.wanandroid.model.Banner
import com.xiangaoole.android.wanandroid.model.HomeArticle
import com.xiangaoole.android.wanandroid.ui.ArticleActivity
import com.xiangaoole.android.wanandroid.util.bindUrl
import com.xiangaoole.android.wanandroid.util.htmlText
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import timber.log.Timber

class HomeArticleListAdapter(
    private val lifecycleOwner: LifecycleOwner
) : PagingDataAdapter<ListItemModel, ViewHolder>(DIFF) {
    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ListItemModel>() {
            override fun areItemsTheSame(oldItem: ListItemModel, newItem: ListItemModel): Boolean {
                return (oldItem is ListItemModel.Header && newItem is ListItemModel.Header) ||
                        (oldItem is ListItemModel.Item && newItem is ListItemModel.Item &&
                                oldItem.data.id == newItem.data.id)
            }

            override fun areContentsTheSame(
                oldItem: ListItemModel,
                newItem: ListItemModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val model = getItem(position)) {
            is ListItemModel.Header -> (holder as HomeHeaderViewHolder).bind(model.banners)
            is ListItemModel.Item -> (holder as HomeArticleViewHolder).bind(model.data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == R.layout.list_item_home) {
            HomeArticleViewHolder.create(parent)
        } else {
            HomeHeaderViewHolder.create(parent, lifecycleOwner)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItemModel.Header -> R.layout.banner_home
            is ListItemModel.Item -> R.layout.list_item_home
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }
}

/**
 * ViewHolder for List header
 */
class HomeHeaderViewHolder(
    private val binding: BannerHomeBinding,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(binding.root) {

    private val clickListener =
        OnBannerListener<Banner> { data, _ ->
            data?.let {
                ArticleActivity.start(
                    binding.root.context,
                    id = it.id.toLong(),
                    title = it.title,
                    url = it.url
                )
            } ?: Timber.w("onClick when data is null")
        }

    init {
        binding.banner.apply {
            addBannerLifecycleObserver(lifecycleOwner)
            indicator = CircleIndicator(context)
            setAdapter(BannerAdapter())
            setOnBannerListener(clickListener)
        }
    }

    fun bind(banners: List<Banner>?) {
        binding.banner.setDatas(banners)
    }

    companion object {
        fun create(parent: ViewGroup, lifecycleOwner: LifecycleOwner): HomeHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = BannerHomeBinding.inflate(layoutInflater, parent, false)
            return HomeHeaderViewHolder(binding, lifecycleOwner)
        }
    }
}

/**
 * ViewHolder for List body
 */
class HomeArticleViewHolder private constructor(
    private val binding: ListItemHomeBinding
) : ViewHolder(binding.root) {

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
            binding.ivThumbnail.isVisible = envelopePic.isNotBlank()
            if (envelopePic.isNotBlank()) {
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
        fun create(parent: ViewGroup): HomeArticleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemHomeBinding.inflate(layoutInflater, parent, false)
            return HomeArticleViewHolder(binding)
        }
    }
}