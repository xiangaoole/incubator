package com.xiangaoole.android.wanandroid.ui

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.ActivityWanandroidBinding
import com.xiangaoole.android.wanandroid.databinding.LayoutNavHeaderBinding
import com.xiangaoole.android.wanandroid.ui.common.OnScrollToTop
import com.xiangaoole.android.wanandroid.util.DialogUtil
import com.xiangaoole.android.wanandroid.util.Preference
import com.xiangaoole.android.wanandroid.util.StatusBarUtil
import com.xiangaoole.android.wanandroid.viewmodel.WanAndroidViewModel
import com.xiangaoole.android.wanandroid.widget.CustomToast

/**
 * The Home Activity for WanAndroid.
 */
class WanAndroidActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWanandroidBinding

    private lateinit var navHeaderBinding: LayoutNavHeaderBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var fragmentManager: FragmentManager

    private val viewModel: WanAndroidViewModel by viewModels {
        val repository = Injection.provideWanAndroidRepository(this)
        WanAndroidViewModel.Factory(repository, application)
    }

    /**
     * DrawerLayout menu 点击事件
     */
    private val mDrawerNavItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.collection -> {
                    Intent(this, CollectionActivity::class.java).run {
                        startActivity(this)
                    }
                    true
                }
                R.id.logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

    private fun logout() {
        DialogUtil.getConfirmDialog(
            this, getString(R.string.confirm_logout)
        ) { _, _ ->
            viewModel.logout()
        }.show()
    }

    private fun logoutSuccess(success: Boolean) {
        if (success) {
            Preference.saveLoginData(null)
            CustomToast(this, getString(R.string.logout_success)).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWanandroidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBar()

        // Find NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        fragmentManager = navHostFragment.childFragmentManager

        // Toolbar
        appBarConfiguration =
            AppBarConfiguration(
                setOf(R.id.homeFragment, R.id.wechatFragment, R.id.projectFragment),
                binding.drawerLayout
            )
        binding.includedToolbar.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Bottom Navigation
        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
        }

        binding.fabButton.setOnClickListener(::onFabButtonClick)

        initViewModelObserve()
        initDrawerNavView()
    }

    private fun setStatusBar() {
        val typedValue = TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        val color = ContextCompat.getColor(this, typedValue.resourceId)

        StatusBarUtil.setStatusBarColor(window, color)
        StatusBarUtil.setLightStatusBar(window, false)
    }

    private fun initViewModelObserve() {
        viewModel.logoutSuccess.observe(this) {
            if (it != null) {
                logoutSuccess(it)
                viewModel.logoutSuccessDone()
            }
        }
        viewModel.error.observe(this) {
            if (it != null) {
                CustomToast(this, it)
                viewModel.errorDone()
            }
        }
    }

    private fun initDrawerNavView() {
        binding.navView.setNavigationItemSelectedListener(mDrawerNavItemSelectedListener)
        navHeaderBinding = LayoutNavHeaderBinding.bind(binding.navView.getHeaderView(0))

        viewModel.isLogin.observe(this) {
            binding.navView.menu.findItem(R.id.logout).isVisible = it
            navHeaderBinding.tvUserName.text =
                if (it == true) {
                    viewModel.username.value
                } else {
                    getString(R.string.not_login)
                }
        }

        navHeaderBinding.layout.setOnClickListener {
            // 登录
            viewModel.isLogin.value?.let {
                if (!it) {
                    Intent(this@WanAndroidActivity, LoginActivity::class.java).run {
                        startActivityForResult(this, REQUEST_LOGIN)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun onFabButtonClick(view: View) {
        navController.currentDestination?.id?.let {
            val currentFragment = fragmentManager.fragments[0] as? OnScrollToTop
            currentFragment?.scrollToTop(view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            viewModel.loadLoginData()
        }
    }

    companion object {
        private const val REQUEST_LOGIN: Int = 1
    }
}