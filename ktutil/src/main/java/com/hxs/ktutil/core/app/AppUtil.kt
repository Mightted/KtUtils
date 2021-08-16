package com.hxs.ktutil.core.app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import com.hxs.ktutil.BuildConfig
import com.hxs.ktutil.KtUtilEntrance


object AppUtil {

    fun context() = KtUtilEntrance.context()

    fun getPackageName(): String? = context()?.packageName

    fun packageManager():PackageManager? = context()?.packageManager

    fun startActivity(intent: Intent) {

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context()?.startActivity(intent)

    }


    /**
     * 跳转到指定应用的指定页面
     * [context] 当前应用上下文
     * [packageName] 要跳转的应用
     * [activityDir] 要跳转的页面,为空时跳转到应用首页
     */
    fun jumpActivity(context: Context, packageName: String, activityDir: String = "") {
        if (TextUtils.isEmpty(activityDir)) {
            context.packageManager.getLaunchIntentForPackage(packageName)?.let {
                context.startActivity(it)
            }
            return
        }
        val intent = Intent()
        intent.component = ComponentName(packageName, activityDir)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    fun toAppSettingPage(context: Context, packageName: String) {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            context.startActivity(intent)
        }
    }

    fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }


}