package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.content.Context
import android.os.Build
import com.hxs.ktutil.core.app.AppUtil
import com.hxs.ktutil.core.device.HardwareUtil

class MeizuBrandImpl:CommonBrandImpl() {

    /**
     * 判断魅族设备的刘海屏状态
     * 找不到官方文档，这是根据一个开发者拿到的资料
     * https://www.jianshu.com/p/06673f2f743f
     */
    override fun hasNotchInScreen(activity: Activity): NotchState {
        if (HardwareUtil.higherBuildVersion(Build.VERSION_CODES.P)) {
            return super.hasNotchInScreen(activity)
        }

        return try {
            val clazz = Class.forName("flyme.config.FlymeFeature")
            val field = clazz.getDeclaredField("IS_FRINGE_DEVICE")
            val isNotch = field.get(null) as Boolean

            val res = activity.resources
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

    /**
     * 跳转魅族手机管家
     * 操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
     */
    override fun navigate2WhiteListSetting(context: Context) {
        super.navigate2WhiteListSetting(context)
        AppUtil.jumpActivity(context, "com.meizu.safe")
    }
}