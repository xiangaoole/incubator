package com.xiangaoole.android.wanandroid.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.ActivityWanandroidBinding

/**
 * The Home Activity for WanAndroid. 
 */
class WanAndroidActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWanandroidBinding

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var fragmentManager: FragmentManager

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
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.wechatFragment, R.id.projectFragment))
        binding.includedToolbar.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Bottom Navigation
        binding.bottomNavigationView.apply {
            setupWithNavController(navController)
        }

        binding.fabButton.setOnClickListener(::onFabButtonClick)

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

    interface ChildFragmentInterface {
        fun scrollToTop(view: View)
    }

}