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
import com.hxs.ktutil.core.manufacturer.BrandHelper


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
        BrandHelper.navigate2WhiteListSetting(context)
    }




















}