package com.xiangaoole.android.wanandroid.ui.article

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xiangaoole.android.wanandroid.Constant
import com.xiangaoole.android.wanandroid.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private var id: Long = -1L
    private var title: String = ""
    private var url: String = ""

    private lateinit var binding: ActivityArticleBinding

    companion object {
        fun start(context: Context, id: Long, title: String, url: String) {
            Intent(context, ArticleActivity::class.java).apply {
                putExtra(Constant.CONTENT_ID_KEY, id)
                putExtra(Constant.CONTENT_TITLE_KEY, title)
                putExtra(Constant.CONTENT_URL_KEY, url)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        intent.extras?.let {
            id = it.getLong(Constant.CONTENT_ID_KEY, -1L)
            title = it.getString(Constant.CONTENT_TITLE_KEY, "")
            url = it.getString(Constant.CONTENT_URL_KEY, "")
        }

        binding.webView.loadUrl(url)
    }
}