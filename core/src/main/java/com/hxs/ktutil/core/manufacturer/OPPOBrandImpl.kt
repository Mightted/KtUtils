package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.os.Build
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
}