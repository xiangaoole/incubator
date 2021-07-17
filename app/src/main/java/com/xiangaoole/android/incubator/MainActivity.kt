package com.xiangaoole.android.incubator

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.xiangaoole.android.meditation.MeditationActivity
import com.xiangaoole.android.module_home.HomeActivity
import com.xiangaoole.android.wanandroid.ui.WanAndroidActivity
import com.xiangaoole.android.wanandroid.util.StatusBarUtil
import timber.log.Timber
import java.net.URI
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("MainActivity onCreate")
        setStatusBar()
        checkPermission()
    }

    fun toHome(view: View) {
        jumpTo(HomeActivity::class)
    }

    fun toMeditation(view: View) {
        jumpTo(MeditationActivity::class)
    }

    fun toWanAndroid(view: View) {
        jumpTo(WanAndroidActivity::class)
    }

    private fun jumpTo(activityClass: KClass<*>) {
        startActivity(Intent(this, activityClass.java))
    }

    fun toTestTask(view: View) {
        jumpTo(HelloRnActivity::class)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.d("onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.d("onSaveInstanceState")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.d("onConfigurationChanged")
    }

    private fun setStatusBar() {
        val typedValue = TypedValue();
        theme.resolveAttribute(R.attr.colorSurface, typedValue, true);
        val color = ContextCompat.getColor(this, typedValue.resourceId)

        StatusBarUtil.setStatusBarColor(window, color)
        StatusBarUtil.setLightStatusBar(window, true)
    }

    private fun checkPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}")
            )
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "没有授权悬浮窗(overlay)权限", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val OVERLAY_PERMISSION_REQ_CODE: Int = 1
    }
}