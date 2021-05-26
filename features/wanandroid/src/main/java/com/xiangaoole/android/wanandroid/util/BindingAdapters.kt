package com.xiangaoole.android.wanandroid.util

import android.text.Spanned
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.xiangaoole.android.wanandroid.R

val options = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.DATA)
    .placeholder(R.drawable.loading_animation)
    .error(R.drawable.ic_broken_image_24)


@BindingAdapter("bindUrl")
fun ImageView.bindUrl(url: String) {
    Glide.with(this)
        .load(url)
        .apply(options)
        .into(this)
}

@BindingAdapter("htmlText")
fun TextView.htmlText(text: String) {
    setText(text.fromHtml())
}

fun String.fromHtml(): Spanned =
    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
