package com.hxs.ktutil.core.app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.text.TextUtils


object AppUtil {

    fun getPackageName(context: Context): String = context.packageName


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

}