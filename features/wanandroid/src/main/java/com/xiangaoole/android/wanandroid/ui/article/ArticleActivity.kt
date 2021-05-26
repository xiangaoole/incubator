package com.xiangaoole.android.wanandroid.ui.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.xiangaoole.android.wanandroid.Constant
import com.xiangaoole.android.wanandroid.R
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

        binding.webView.apply {
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }.loadUrl(url)

        binding.includedLayout.apply {
            tvTitle.text = title
            tvTitle.isSelected = true
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        binding.webView.let {
            if (it.canGoBack()) {
                it.goBack()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_browser -> {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(url)
                    startActivity(this)
                }
            }
            R.id.action_share -> {
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.share_article_url,
                            getString(R.string.app_name), title, url
                        )
                    )
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.share)))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}