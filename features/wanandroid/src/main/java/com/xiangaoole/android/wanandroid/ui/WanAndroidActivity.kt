package com.xiangaoole.android.wanandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.xiangaoole.android.wanandroid.util.Preference
import com.xiangaoole.android.wanandroid.viewmodel.WanAndroidViewModel
import timber.log.Timber

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

    private val mDrawerNavItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    Preference.saveLoginData(application, null)
                    viewModel.loadLoginData()
                    true
                }
                else -> false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWanandroidBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        initDrawerNavView()
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
            val currentFragment = fragmentManager.fragments[0] as? ChildFragmentInterface
            currentFragment?.scrollToTop(view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            viewModel.loadLoginData()
        }
    }

    interface ChildFragmentInterface {
        fun scrollToTop(view: View)
    }

    companion object {
        private const val REQUEST_LOGIN: Int = 1
    }
}