package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.os.Build
import android.text.TextUtils
import com.hxs.ktutil.core.device.HardwareUtil

class SamsungBrandImpl : CommonBrandImpl() {

    /**
     * 判断三星设备的刘海屏，钻孔屏状态
     * http://support-cn.samsung.com/App/DeveloperChina/Notice/Detail?NoticeId=86
     */
    override fun hasNotchInScreen(activity: Activity): NotchState {
        if (HardwareUtil.higherBuildVersion(Build.VERSION_CODES.P)) {
            return super.hasNotchInScreen(activity)
        }

        return try {

            val res = activity.resources
            val resId = res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android")
            val spec = if (resId > 0) res.getString(resId) else null
            val hasNotch = spec != null && !TextUtils.isEmpty(spec)
            NotchState(hasNotch, if (hasNotch) getStatusBarHeight(activity) else 0)
        } catch (e: Exception) {
            println(e.message)
            NotchState()
        }
    }


}