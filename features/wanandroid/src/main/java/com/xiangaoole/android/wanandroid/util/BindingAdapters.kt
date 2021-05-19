package com.xiangaoole.android.wanandroid.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bindUrl")
fun ImageView.bindUrl(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}

@BindingAdapter("htmlText")
fun TextView.htmlText(text: String) {
    setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT))
}