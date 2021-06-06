package com.xiangaoole.android.wanandroid.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xiangaoole.android.wanandroid.databinding.BannerImageTitleBinding
import com.xiangaoole.android.wanandroid.model.Banner
import com.youth.banner.adapter.BannerAdapter

/**
 * Banner adapter.
 */
class BannerAdapter : BannerAdapter<Banner, BannerViewHolder>(null) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        return BannerViewHolder.create(parent!!)
    }

    override fun onBindView(holder: BannerViewHolder?, data: Banner?, position: Int, size: Int) {
        holder?.bind(data)
    }

}

/**
 * Banner View with Image and Title.
 */
class BannerViewHolder(
    val binding: BannerImageTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(banner: Banner?) {
        banner?.let {
            binding.banner = banner
            binding.executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup): BannerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = BannerImageTitleBinding.inflate(inflater, parent, false)
            return BannerViewHolder(binding)
        }
    }
}