package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.content.Context

interface BrandInterface {


    /**
     * 判断屏幕上端是否带有缺口（刘海屏，挖孔屏）
     */
    fun hasNotchInScreen(activity: Activity): NotchState

    fun getStatusBarHeight(context: Context):Int
}

data class NotchState(var hasNotch: Boolean = false, var height: Int = 0)