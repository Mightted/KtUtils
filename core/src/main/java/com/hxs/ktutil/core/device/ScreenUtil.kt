package com.hxs.ktutil.core.device

import android.app.Activity
import com.hxs.ktutil.core.manufacturer.BrandHelper
import com.hxs.ktutil.core.manufacturer.NotchState

object ScreenUtil {

    /**
     * 判断屏幕上端是否带有缺口（刘海屏，挖孔屏）
     */
    fun hasNotchInScreen(activity: Activity): NotchState {
        return BrandHelper.hasNotchInScreen(activity)
        // android P 以上有标准 API 来判断是否有刘海屏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            val insets = activity.window.decorView.rootWindowInsets
//                ?: throw RuntimeException("RootWindowInsets值为空，请在onAttachedToWindow方法中调用")
//            // 这个有值说明有刘海屏
//            val notchState = NotchState()
//            insets.displayCutout?.let {
//                notchState.hasNotch = true
//                notchState.height = it.boundingRectTop.bottom
//            }
//            return notchState
//
//
//        } else {
//            // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo,三星和魅族
//            return when (HardwareUtil.brand()) {
//                HUAWEI -> hasNotchHw(activity)
//                XIAOMI -> hasNotchXiaoMi(activity)
//                OPPO -> hasNotchOPPO(activity)
//                VIVO -> hasNotchVIVO()
//                SAMSUNG -> hasNotchSamsung(activity)
//                MEIZU -> hasNotchMeizu(activity.resources)
//                else -> NotchState()
//            }
//        }
    }

}