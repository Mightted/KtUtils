package com.hxs.ktutil.core.manufacturer

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import com.hxs.ktutil.core.device.HardwareUtil

class VIVOBrandImpl:CommonBrandImpl() {

    override fun hasNotchInScreen(activity: Activity): NotchState {
        if (HardwareUtil.higherBuildVersion(Build.VERSION_CODES.P)) {
            return super.hasNotchInScreen(activity)
        }
        return hasNotchVIVO()
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
    fun hasRoundScreenVIVO(): Boolean {
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
}