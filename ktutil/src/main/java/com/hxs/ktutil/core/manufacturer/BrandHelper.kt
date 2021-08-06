package com.hxs.ktutil.core.manufacturer

import android.app.Activity
import android.content.Context
import com.hxs.ktutil.core.device.BRAND.*
import com.hxs.ktutil.core.device.HardwareUtil

object BrandHelper : BrandInterface {
    private val brand: BrandInterface by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        when (HardwareUtil.brand()) {
            HUAWEI -> HuaweiBrandImpl()
            XIAOMI -> XiaoMiBrandImpl()
            OPPO -> OPPOBrandImpl()
            VIVO -> VIVOBrandImpl()
            SAMSUNG -> SamsungBrandImpl()
            MEIZU -> MeizuBrandImpl()
            else -> CommonBrandImpl()
        }
    }


    override fun hasNotchInScreen(activity: Activity) = brand.hasNotchInScreen(activity)

    override fun getStatusBarHeight(context: Context) = brand.getStatusBarHeight(context)


    override fun navigate2WhiteListSetting(context: Context) {
        brand.navigate2WhiteListSetting(context)
    }
}

