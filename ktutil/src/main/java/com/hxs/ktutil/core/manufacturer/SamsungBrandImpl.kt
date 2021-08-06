package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.hxs.ktutil.core.app.AppUtil
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

    /**
     * 跳转三星智能管理器
     * 操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
     */

    override fun navigate2WhiteListSetting(context: Context) {
        super.navigate2WhiteListSetting(context)

        try {
            AppUtil.jumpActivity(context, "com.samsung.android.sm_cn")
        } catch (e: java.lang.Exception) {
            AppUtil.jumpActivity(context, "com.samsung.android.sm")
        }

    }


}