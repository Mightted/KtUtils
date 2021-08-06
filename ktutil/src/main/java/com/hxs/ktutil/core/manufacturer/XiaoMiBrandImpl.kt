package com.hxs.ktutil.core.manufacturer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import com.hxs.ktutil.core.app.AppUtil
import com.hxs.ktutil.core.device.HardwareUtil

class XiaoMiBrandImpl : CommonBrandImpl() {

    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     */
    @SuppressLint("PrivateApi")
    override fun hasNotchInScreen(activity: Activity): NotchState {
        if (HardwareUtil.higherBuildVersion(Build.VERSION_CODES.P)) {
            return super.hasNotchInScreen(activity)
        }

        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            val get =
                clazz.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            val hasNotch = get.invoke(clazz, "ro.miui.notch", 0) as Int == 1
            val notchHeight = getMIUINotchHeight(activity)
            NotchState(hasNotch, if (hasNotch) notchHeight else 0)


        } catch (e: java.lang.Exception) {
            println(e.message)
            NotchState()
        }
    }


    /**
     * 获取MIUI刘海屏或者水滴屏高度
     * [res] 系统上下文
     */
    private fun getMIUINotchHeight(context: Context): Int {
        // MIUI 10 新增了获取刘海宽和高的方法，需升级至8.6.26开发版及以上版本。
        val res = context.resources
        var resourceId: Int = res.getIdentifier("notch_height", "dimen", "android")
        return if (resourceId == 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            // 如果实在是获取不到刘海屏高度，就使用与其相似的状态栏高度来算吧
            getStatusBarHeight(context)
        }
    }

    /**
     * 跳转小米安全中心的自启动管理页面
     * 操作步骤：授权管理 -> 自启动管理 -> 允许应用自启动
     */
    override fun navigate2WhiteListSetting(context: Context) {
        super.navigate2WhiteListSetting(context)
        AppUtil.jumpActivity(
            context,
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    }
}