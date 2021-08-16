package com.hxs.ktutil.core.device

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import com.hxs.ktutil.core.app.AppUtil

const val NETWORK_NO = -1
const val NETWORK_2G = 2
const val NETWORK_3G = 3
const val NETWORK_4G = 4

object NetworkUtil {


    private const val NETWORK_TYPE_GSM = 16
    private const val NETWORK_TYPE_IWLAN = 18
    private const val NETWORK_TYPE_TD_SCDMA = 17
    const val NETWORK_UNKNOWN = 5
    const val NETWORK_WIFI = 1
    var typeList = mutableListOf<String>()
    fun openWirelessSettings(context: Context) {
        context.startActivity(Intent("android.settings.SETTINGS"))
    }

    fun getActiveNetworkInfo(context: Context?): NetworkInfo? {
        return (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

    }

    val isAvailable: Boolean
        get() {
            val info = getActiveNetworkInfo(AppUtil.context())
            return info != null && info.isAvailable
        }

    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    fun is4G(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable && info.subtype == 13
    }

    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo!!.type == 1
    }

    fun getNetworkOperatorName(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm?.networkOperatorName
    }

    fun getPhoneType(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType
    }

    fun getNetWorkType(context: Context): Int {
        val info = getActiveNetworkInfo(context)
        if (info == null || !info.isAvailable) {
            return -1
        }
        if (info.type == 1) {
            return 1
        }
        return if (info.type != 0) {
            5
        } else when (info.subtype) {
            1, 2, 4, 7, 11, 16 -> 2
            3, 5, 6, 8, 9, 10, 12, 14, 15, 17 -> 3
            13, 18 -> 4
            else -> {
                val subtypeName = info.subtypeName
                if (subtypeName.equals("TD-SCDMA", ignoreCase = true) || subtypeName.equals(
                        "WCDMA",
                        ignoreCase = true
                    ) || subtypeName.equals("CDMA2000", ignoreCase = true)
                ) {
                    3
                } else 5
            }
        }
    }

    fun getNetWorkTypeName(context: Context): String {
        val netWorkType = getNetWorkType(context)
        return if (netWorkType == -1) {
            "NETWORK_NO"
        } else when (netWorkType) {
            1 -> "NETWORK_WIFI"
            2 -> "NETWORK_2G"
            3 -> "NETWORK_3G"
            4 -> "NETWORK_4G"
            else -> "NETWORK_UNKNOWN"
        }
    }

    init {
        typeList.add("打开网络设置界面")
        typeList.add("获取活动网络信息")
        typeList.add("判断网络是否可用")
        typeList.add("判断网络是否是4G")
        typeList.add("判断wifi是否连接状态")
        typeList.add("获取移动网络运营商名称")
        typeList.add("获取当前的网络类型")
        typeList.add("获取当前的网络类型(WIFI,2G,3G,4G)")
    }
}