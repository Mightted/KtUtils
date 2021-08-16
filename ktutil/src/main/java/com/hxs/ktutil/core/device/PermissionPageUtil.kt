package com.hxs.ktutil.core.device

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import com.hxs.ktutil.core.app.AppUtil
import com.hxs.ktutil.core.misc.LogUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 前往应用详情页
 */
object PermissionPageUtil {
//    const val TAG = "PermissionPageManager"

    fun jumpPermissionPage() {
        val name = Build.MANUFACTURER
        LogUtil.debug("jumpPermissionPage --- name : $name")
        when (name) {
            "HUAWEI" -> goHuaWeiMainPage()
            "vivo" -> goVivoMainPage()
            "OPPO" -> goOppoMainPage()
            "Coolpad" -> goCoolpadMainPage()
            "Meizu" -> goMeizuMainPage()
            "Xiaomi" -> goXiaoMiMainPage()// Redmi Note 5A android 7.1.2 验证通过
            "samsung" -> goSangXinMainPage()
            "Sony" -> goSonyMainPage()
            "LG" -> goLGMainPage()
            else -> goIntentSetting()

        }
    }

    private fun goLGMainPage() {
        try {
            val intent = Intent(AppUtil.getPackageName())
            intent.component = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AccessLockSummaryActivity"
            )
            AppUtil.startActivity(intent)
        } catch (e: Exception) {
            LogUtil.error(e)
            goIntentSetting()
        }
    }

    private fun goSonyMainPage() {
        try {
            val intent = Intent(AppUtil.getPackageName())
            intent.component =
                ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            AppUtil.startActivity(intent)
        } catch (e: Exception) {
            LogUtil.error(e)
            goIntentSetting()
        }
    }

    private fun goHuaWeiMainPage() {
        try {
            val intent = Intent(AppUtil.getPackageName())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.component = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
            AppUtil.startActivity(intent)
        } catch (e: Exception) {
            LogUtil.error(e)
            goIntentSetting()
        }
    }

    private fun getMiuiVersion(): String {
        return try {
            val propName = "ro.miui.ui.version.name"
            val p = Runtime.getRuntime().exec("getprop $propName")
            BufferedReader(
                InputStreamReader(p.inputStream), 1024
            ).use {
                it.readLine()
            }


        } catch (e: IOException) {
            LogUtil.error(e)
            ""
        }
    }

    private fun goXiaoMiMainPage() {
        val rom = getMiuiVersion()
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.putExtra("extra_pkgname", AppUtil.getPackageName())
        if ("V6" == rom || "V7" == rom) {
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
            )
        } else if ("V8" == rom || "V9" == rom) {
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
        } else {
            goIntentSetting()
        }
        AppUtil.startActivity(intent)
    }

    private fun goMeizuMainPage() {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory("android.intent.category.DEFAULT")
            intent.putExtra("packageName", AppUtil.getPackageName())
            AppUtil.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            LogUtil.error(e)
            goIntentSetting()
        }
    }

    private fun goSangXinMainPage() {
        goIntentSetting()
    }

    private fun goIntentSetting() {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.data = Uri.fromParts("package", AppUtil.getPackageName(), null)
        try {
            AppUtil.startActivity(intent)
        } catch (e: Exception) {
            LogUtil.error(e)
        }
    }

    private fun goOppoMainPage() {
        doStartApplicationWithPackageName("com.coloros.safecenter")
    }

    private fun goCoolpadMainPage() {
        doStartApplicationWithPackageName("com.yulong.android.security:remote")
    }

    private fun goVivoMainPage() {
        doStartApplicationWithPackageName("com.bairenkeji.icaller")
    }


    private fun doStartApplicationWithPackageName(packName: String) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageInfo: PackageInfo? = null

        try {
            packageInfo = AppUtil.packageManager()?.getPackageInfo(packName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtil.error(e)
        }
        if (packageInfo == null) {
            return
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageInfo.packageName)
        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveInfoList =
            AppUtil.packageManager()?.queryIntentActivities(resolveIntent, 0) ?: return
        LogUtil.debug("resolveInfoList${resolveInfoList.size}")
//        for (item in resolveInfoList) {
//            LogUtil.debug(item.activityInfo.packageName + item.activityInfo.name)
//        }
        val resolveInfo = resolveInfoList.iterator().next()
        if (resolveInfo != null) {
            // packageName参数2 = 参数 packname
            val packageName = resolveInfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityName]
            val className = resolveInfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            val cn = ComponentName(packageName, className)
            intent.component = cn
            try {
                AppUtil.startActivity(intent)
            } catch (e: Exception) {
                LogUtil.error(e)
                goIntentSetting()
            }
        }
    }
}
