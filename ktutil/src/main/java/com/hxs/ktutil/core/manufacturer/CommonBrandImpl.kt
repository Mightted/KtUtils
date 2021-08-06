package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

open class CommonBrandImpl : BrandInterface {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun hasNotchInScreen(activity: Activity): NotchState {
        val insets = activity.window.decorView.rootWindowInsets
            ?: throw RuntimeException("RootWindowInsets值为空，请在onAttachedToWindow方法中调用")
        // 这个有值说明有刘海屏
        val notchState = NotchState()
        insets.displayCutout?.let {
            notchState.hasNotch = true
            notchState.height = it.boundingRectTop.bottom
        }
        return notchState
    }


    /**
     * 获取状态栏高度
     */
    override fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val res = context.resources
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    override fun navigate2WhiteListSetting(context: Context) {

    }
}