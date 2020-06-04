package com.hxs.ktutil.core.device

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import java.util.*

object ScreenUtil {

    data class NotchState(var hasNotch: Boolean = false, var height: Int = 0)

    private const val HUAWEI = "huawei"
    private const val XIAOMI = "xiaomi"
    private const val OPPO = "oppo"
    private const val VIVO = "vivo"


    /**
     * 判断屏幕上端是否带有缺口（刘海屏，挖孔屏）
     */
    fun hasNotchInScreen(activity: Activity): NotchState {
        // android P 以上有标准 API 来判断是否有刘海屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val insets = activity.window.decorView.rootWindowInsets
                ?: throw RuntimeException("RootWindowInsets值为空，请在onAttachedToWindow方法中调用")
            // 这个有值说明有刘海屏
            val notchState = NotchState()
            insets.displayCutout?.let {
                notchState.hasNotch = true
                notchState.height = it.boundingRectTop.bottom
            }
            return notchState


        } else {
            // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo
            return when (Build.MANUFACTURER.toLowerCase(Locale.ROOT)) {
                HUAWEI -> hasNotchHw(activity)
                XIAOMI -> hasNotchXiaoMi(activity)
                OPPO -> hasNotchOPPO(activity)
                VIVO -> hasNotchVIVO()
                else -> NotchState()
            }
        }
    }

    /**
     * 判断是否是刘海屏
     */
    private fun hasNotchVIVO(): NotchState {
        val hasNotch = getVIVOFeature(0x20)
        return NotchState(hasNotch, if (hasNotch) 100 else 0)

    }

    /**
     * 判断屏幕是否有圆角
     */
    private fun hasRoundScreenVIVO(): Boolean {
        return getVIVOFeature(0x8)
    }


    /**
     * 判断vivo是否有全面屏特性, 不建议直接调用这个方法
     * https://swsdl.vivo.com.cn/appstore/developer/uploadfile/20180328/20180328152252602.pdf
     * @param featureValue 特性值， 0x20为带有刘海，0x8带有圆角
     */
    @SuppressLint("PrivateApi")
    fun getVIVOFeature(featureValue: Int): Boolean {
        return try {
            val clazz = Class.forName("android.util.FtFeature")
            val get = clazz.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            get.invoke(clazz, featureValue) as Boolean
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断oppo是否有刘海屏
     * https://open.oppomobile.com/wiki/doc#id=10159
     */
    private fun hasNotchOPPO(activity: Activity): NotchState {
        val hasNotch =
            activity.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
        return NotchState(hasNotch, if (hasNotch) 80 else 0)
    }


    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     */
    @SuppressLint("PrivateApi")
    private fun hasNotchXiaoMi(activity: Activity): NotchState {
        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            val get =
                clazz.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            val hasNotch = get.invoke(clazz, "ro.miui.notch", 0) as Int == 1
            val notchHeight = getStatusBarHeight(activity)
            NotchState(hasNotch, notchHeight)


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            NotchState()
        }
    }

    private fun getStatusBarHeight(context: Activity): Int {
        var statusBarHeight = 0
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }


    /**
     * 判断华为是否有刘海屏
     * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
     */
    private fun hasNotchHw(activity: Activity): NotchState {
        return try {
            val cl = activity.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val hasNotchMethod = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            val hasNotch = hasNotchMethod.invoke(hwNotchSizeUtil) as Boolean
            val notchHeightMethod = hwNotchSizeUtil.getMethod("HwNotchSizeUtil")
            val notchHeight = notchHeightMethod.invoke(hwNotchSizeUtil) as Int
            NotchState(hasNotch, notchHeight)

        } catch (e: Exception) {
            NotchState()
        }
    }


    // 获取系统栏高度
    fun detectSystemBarHeight(
        view: View, callback: (statusBarHeight: Int, navigationBarHeight: Int) -> Unit
    ) {
        view.setOnApplyWindowInsetsListener { _, insets: WindowInsets? ->
            var statusBarHeight = 0
            var navigationBarHeight = 0
            insets?.run {
                statusBarHeight = systemWindowInsetTop
                navigationBarHeight = systemWindowInsetBottom
            }

            callback.invoke(statusBarHeight, navigationBarHeight)
            insets
        }
    }

}