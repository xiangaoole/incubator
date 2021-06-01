package com.xiangaoole.android.wanandroid.ui.article

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.color.MaterialColors
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.NestedScrollAgentWebView
import com.just.agentweb.WebChromeClient
import com.xiangaoole.android.wanandroid.Constant
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    private var id: Long = -1L
    private var title: String = ""
    private var url: String = ""

    private lateinit var binding: ActivityArticleBinding

    private lateinit var mAgentWeb: AgentWeb

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            binding.includedToolbar.tvTitle.text = title
        }
    }

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
        if (savedInstanceState == null) {
            mAgentWeb.urlLoader.loadUrl(url)
        } else {
            mAgentWeb.webCreator.webView.restoreState(savedInstanceState)
        }
    }

    private fun initView() {
        intent.extras?.let {
            id = it.getLong(Constant.CONTENT_ID_KEY, -1L)
            title = it.getString(Constant.CONTENT_TITLE_KEY, "")
            url = it.getString(Constant.CONTENT_URL_KEY, "")
        }

        binding.includedToolbar.apply {
            tvTitle.isVisible = true
            tvTitle.text = title
            tvTitle.isSelected = true
            setSupportActionBar(toolbar)
            //supportActionBar?.title = title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        initWebView()
    }

    private fun initWebView() {
        val layoutParams = CoordinatorLayout.LayoutParams(-1, -1)
        layoutParams.behavior = AppBarLayout.ScrollingViewBehavior()
        val webView = NestedScrollAgentWebView(this)
        val themeColor = MaterialColors.getColor(binding.root, R.attr.colorPrimary)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.root, 1, layoutParams)
            .useDefaultIndicator(themeColor, 2)
            .setWebView(webView)
            .setWebChromeClient(mWebChromeClient)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) // 打开其他应用时，弹窗咨询
            .interceptUnkownUrl()
            .createAgentWeb()
            .get()

        mAgentWeb.webCreator.webView.apply {
            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        mAgentWeb.let {
            if (!it.back()) {
                super.onBackPressed()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
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
                            getString(R.string.module_name), title, url
                        )
                    )
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.share)))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mAgentWeb.webCreator.webView.saveState(outState)
    }

    override fun onPause() {
        super.onPause()
        mAgentWeb.webLifeCycle.onPause()
    }

    override fun onResume() {
        super.onResume()
        mAgentWeb.webLifeCycle.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.includedToolbar.tvTitle.isSelected = false // !! stopMargin
        mAgentWeb.webLifeCycle.onDestroy()
    }
}