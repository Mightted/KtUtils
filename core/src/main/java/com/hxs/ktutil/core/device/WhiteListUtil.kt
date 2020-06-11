package com.hxs.ktutil.core.device

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.hxs.ktutil.core.app.AppUtil.getPackageName
import com.hxs.ktutil.core.app.AppUtil.jumpActivity
import com.hxs.ktutil.core.device.BRAND.*


/**
 * 保活白名单工具类
 */
object WhiteListUtil {

    /**
     * 判断是否在电池优化白名单中
     * 参考帖子：https://juejin.im/post/5dfaeccbf265da33910a441d
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val isIgnoring: Boolean
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName(context))
        return isIgnoring
    }


    /**
     * 请求加入电池优化白名单
     * [context] 上下文，当来自Activity时，允许监听请求结果
     * [requestCode] 只有以监听方式打开页面的时候，该值才有效
     */
    @SuppressLint("BatteryLife")
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestIgnoreBatteryOptimizations(context: Context, requestCode: Int = -1) {
        if (!isIgnoringBatteryOptimizations(context)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:" + getPackageName(context))
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
        }
    }


    fun goWhiteListSetting(context: Context) {
        when (HardwareUtil.brand()) {
            HUAWEI -> goHuaweiSetting(context)
            XIAOMI -> goXiaomiSetting(context)
            OPPO -> goOPPOSetting(context)
            VIVO -> goVIVOSetting(context)
            SAMSUNG -> goSamsungSetting(context)
            MEIZU -> goMeizuSetting(context)
            else -> {
                // 不对非主流厂商做处理
            }
        }
    }


    /**
     * 跳转华为手机管家的启动管理页
     * 操作步骤：应用启动管理 -> 关闭应用开关 -> 打开允许自启动
     */
    private fun goHuaweiSetting(context: Context) {
        try {
            jumpActivity(
                context,
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            )
        } catch (e: Exception) {
            jumpActivity(
                context,
                "com.huawei.systemmanager",
                "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"
            )
        }
    }


    /**
     * 跳转小米安全中心的自启动管理页面
     * 操作步骤：授权管理 -> 自启动管理 -> 允许应用自启动
     */
    private fun goXiaomiSetting(context: Context) {
        jumpActivity(
            context,
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
    }


    /**
     * 跳转OPPO手机管家
     * 操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
     */
    private fun goOPPOSetting(context: Context) {
        try {
            jumpActivity(context, "com.coloros.phonemanager")
        } catch (e1: java.lang.Exception) {
            try {
                jumpActivity(context, "com.oppo.safe")
            } catch (e2: java.lang.Exception) {
                try {
                    jumpActivity(context, "com.coloros.oppoguardelf")
                } catch (e3: java.lang.Exception) {
                    jumpActivity(context, "com.coloros.safecenter")
                }
            }
        }
    }


    /**
     * 跳转VIVO手机管家
     * 操作步骤：权限管理 -> 自启动 -> 允许应用自启动
     */
    private fun goVIVOSetting(context: Context) {
        jumpActivity(context, "com.iqoo.secure")
    }


    /**
     * 跳转魅族手机管家
     * 操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
     */
    private fun goMeizuSetting(context: Context) {
        jumpActivity(context, "com.meizu.safe")
    }


    /**
     * 跳转三星智能管理器
     * 操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
     */
    private fun goSamsungSetting(context: Context) {
        try {
            jumpActivity(context, "com.samsung.android.sm_cn")
        } catch (e: java.lang.Exception) {
            jumpActivity(context, "com.samsung.android.sm")
        }
    }


}