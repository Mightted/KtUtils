package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.content.Context
import android.os.Build
import com.hxs.ktutil.core.app.AppUtil
import com.hxs.ktutil.core.device.HardwareUtil

class OPPOBrandImpl : CommonBrandImpl() {

    /**
     * 判断oppo是否有刘海屏
     * https://open.oppomobile.com/wiki/doc#id=10159
     */
    override fun hasNotchInScreen(activity: Activity): NotchState {
        if (HardwareUtil.higherBuildVersion(Build.VERSION_CODES.P)) {
            return super.hasNotchInScreen(activity)
        }

        val hasNotch =
            activity.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
        return NotchState(hasNotch, if (hasNotch) 80 else 0)
    }


    /**
     * 跳转OPPO手机管家
     * 操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
     */
    override fun navigate2WhiteListSetting(context: Context) {
        super.navigate2WhiteListSetting(context)

        try {
            AppUtil.jumpActivity(context, "com.coloros.phonemanager")
        } catch (e1: java.lang.Exception) {
            try {
                AppUtil.jumpActivity(context, "com.oppo.safe")
            } catch (e2: java.lang.Exception) {
                try {
                    AppUtil.jumpActivity(context, "com.coloros.oppoguardelf")
                } catch (e3: java.lang.Exception) {
                    AppUtil.jumpActivity(context, "com.coloros.safecenter")
                }
            }
        }

    }
}