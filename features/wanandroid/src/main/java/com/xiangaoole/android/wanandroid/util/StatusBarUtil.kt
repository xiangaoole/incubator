package com.xiangaoole.android.wanandroid.util

import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors
import com.xiangaoole.android.wanandroid.R
import timber.log.Timber

object StatusBarUtil {
    private fun fullScreen2(window: Window, resources: Resources) {
        // bar color
        val surfaceColor = MaterialColors.getColor(window.decorView, R.attr.colorSurface)
        window.statusBarColor = surfaceColor
        window.navigationBarColor = surfaceColor

        // icon color
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        Timber.d("nightModeFlags = $nightModeFlags")
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> setDayNight(window, true)
            Configuration.UI_MODE_NIGHT_NO -> setDayNight(window, false)
            else -> Timber.d("nightMode wtf!!")
        }
    }

    private fun setDayNight(window: Window, isNight: Boolean) {
        val decorView = window.decorView
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (isNight) {
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    0, APPEARANCE_LIGHT_STATUS_BARS
                )
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    0, APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            } else {
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS
                )
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_NAVIGATION_BARS, APPEARANCE_LIGHT_NAVIGATION_BARS
                )

            }
        } else {
            if (isNight) {
                decorView.systemUiVisibility = decorView.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv() and
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                decorView.systemUiVisibility = decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    fun setLightStatusBar(window: Window, isLight: Boolean) {
        val decorView = window.decorView
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (isLight) {
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                decorView.windowInsetsController?.setSystemBarsAppearance(
                    0, APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        } else {
            if (isLight) {
                decorView.systemUiVisibility = decorView.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                decorView.systemUiVisibility = decorView.systemUiVisibility and
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }



    fun setStatusBarColor(window: Window, @ColorInt color: Int) {
        window.statusBarColor = color
    }
}