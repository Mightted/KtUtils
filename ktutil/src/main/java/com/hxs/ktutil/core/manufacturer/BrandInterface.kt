package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.content.Context

interface BrandInterface {


    /**
     * 判断屏幕上端是否带有缺口（刘海屏，挖孔屏）
     */
    fun hasNotchInScreen(activity: Activity): NotchState

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int


    /**
     * 跳转厂商定制的后台白名单管理页面
     */
    fun navigate2WhiteListSetting(context: Context)
}

data class NotchState(var hasNotch: Boolean = false, var height: Int = 0)