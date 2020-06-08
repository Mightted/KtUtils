package com.hxs.ktutil.core.device

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils
import java.util.*

object ScreenUtil {

    data class NotchState(var hasNotch: Boolean = false, var height: Int = 0)

    private const val HUAWEI = "huawei"
    private const val XIAOMI = "xiaomi"
    private const val OPPO = "oppo"
    private const val VIVO = "vivo"
    private const val SAMSUNG = "samsung"
    private const val MEIZU = "meizu"


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
            // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo,三星和魅族
            return when (Build.MANUFACTURER.toLowerCase(Locale.ROOT)) {
                HUAWEI -> hasNotchHw(activity)
                XIAOMI -> hasNotchXiaoMi(activity)
                OPPO -> hasNotchOPPO(activity)
                VIVO -> hasNotchVIVO()
                SAMSUNG -> hasNotchSamsung(activity)
                MEIZU -> hasNotchMeizu(activity.resources)
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
            println(e.message)
            false
        }
    }

    /**
     * 判断oppo是否有刘海屏
     * https://open.oppomobile.com/wiki/doc#id=10159
     */
    private fun hasNotchOPPO(context: Context): NotchState {
        val hasNotch =
            context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
        return NotchState(hasNotch, if (hasNotch) 80 else 0)
    }


    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     */
    @SuppressLint("PrivateApi")
    private fun hasNotchXiaoMi(context: Context): NotchState {
        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            val get =
                clazz.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            val hasNotch = get.invoke(clazz, "ro.miui.notch", 0) as Int == 1
            val notchHeight = getMIUINotchHeight(context.resources)
            NotchState(hasNotch, notchHeight)


        } catch (e: java.lang.Exception) {
            println(e.message)
            NotchState()
        }
    }

    /**
     * 获取MIUI刘海屏或者水滴屏高度
     * [res] 系统上下文
     */
    private fun getMIUINotchHeight(res: Resources): Int {
        // MIUI 10 新增了获取刘海宽和高的方法，需升级至8.6.26开发版及以上版本。
        var resourceId: Int = res.getIdentifier("notch_height", "dimen", "android")
        return if (resourceId > 0) {
            resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
            res.getDimensionPixelSize(resourceId)

        } else {
            // 如果实在是获取不到刘海屏高度，就使用与其相似的状态栏高度来算吧
            getStatusBarHeight(res)
        }

    }


    /**
     * 判断华为是否有刘海屏
     * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
     */
    private fun hasNotchHw(context: Context): NotchState {
        return try {
            val cl = context.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val hasNotchMethod = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            val hasNotch = hasNotchMethod.invoke(hwNotchSizeUtil) as Boolean
            val notchHeightMethod = hwNotchSizeUtil.getMethod("HwNotchSizeUtil")
            val notchHeight = notchHeightMethod.invoke(hwNotchSizeUtil) as Int
            NotchState(hasNotch, notchHeight)
        } catch (e: Exception) {
            println(e.message)
            NotchState()
        }
    }


    /**
     * 判断三星设备的刘海屏，钻孔屏状态
     * http://support-cn.samsung.com/App/DeveloperChina/Notice/Detail?NoticeId=86
     */
    private fun hasNotchSamsung(context: Context): NotchState {

        return try {

            val res = context.resources
            val resId = res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android");
            val spec = if (resId > 0) res.getString(resId) else null
            val hasNotch = spec != null && !TextUtils.isEmpty(spec)
            NotchState(hasNotch, if (hasNotch) getMIUINotchHeight(context.resources) else 0)
        } catch (e: Exception) {
            println(e.message)
            NotchState()
        }
    }


    /**
     * 判断魅族设备的刘海屏状态
     * 找不到官方文档，这是根据一个开发者拿到的资料
     * https://www.jianshu.com/p/06673f2f743f
     */
    private fun hasNotchMeizu(res: Resources): NotchState {

        return try {
            val clazz = Class.forName("flyme.config.FlymeFeature")
            val field = clazz.getDeclaredField("IS_FRINGE_DEVICE")
            val isNotch = field.get(null) as Boolean

            var notchHeight = 0
            val resId: Int = res.getIdentifier("fringe_height", "dimen", "android")
            if (resId > 0) {
                notchHeight = res.getDimensionPixelSize(resId)
            }
            NotchState(isNotch, notchHeight)
        } catch (e: Exception) {
            println(e.message)
            NotchState()

        }
    }


    fun getStatusBarHeight(res: Resources): Int {
        var statusBarHeight = 0
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

}