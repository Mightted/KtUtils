package com.hxs.ktutil.core.device

import android.os.Build
import java.util.*

object HardwareUtil {

    fun brandName(): String = Build.MANUFACTURER

    fun brandName2LowerCase(): String = brandName().toLowerCase(Locale.ROOT)

    private fun brandName2UpperCase(): String = brandName().toUpperCase(Locale.ROOT)

    fun brand(): BRAND {
        return try {
            BRAND.valueOf(brandName2UpperCase())
        } catch (e: Exception) {
            BRAND.OTHER
        }
    }


    fun higherBuildVersion(targetVer: Int) = Build.VERSION.SDK_INT >= targetVer

}

enum class BRAND(brandName: String) {

    HUAWEI("huawei"),
    XIAOMI("xiaomi"),
    OPPO("oppo"),
    VIVO("vivo"),
    SAMSUNG("samsung"),
    MEIZU("meizu"),
    OTHER(HardwareUtil.brandName2LowerCase())

}