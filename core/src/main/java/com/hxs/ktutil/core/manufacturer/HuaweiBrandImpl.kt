package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.os.Build
import com.hxs.ktutil.core.device.HardwareUtil

class HuaweiBrandImpl : CommonBrandImpl() {

    /**
     * 判断华为是否有刘海屏
     * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
     */
    override fun hasNotchInScreen(activity: Activity): NotchState {
        if (HardwareUtil.higherBuildVersion(Build.VERSION_CODES.P)) {
            return super.hasNotchInScreen(activity)
        }
        return try {
            val cl = activity.classLoader
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
}