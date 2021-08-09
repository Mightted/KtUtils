package com.hxs.ktutil.core.misc

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.hxs.ktutil.BuildConfig
import java.io.File

object FileUtil {

    // /data/user/0/com.hxs.ktutils/app_xxx
    fun getAppRootPath(context: Context, file_suffix: String): File {
        val file = context.getDir(file_suffix, Context.MODE_PRIVATE)
        println(file.path)
        return file
    }

    fun getFileList(context: Context) {
        context.noBackupFilesDir
        val list = context.fileList()
        list.forEach {
            print(it)
        }
    }


    fun openExcel(context: Context, file: File) {

        val intent = Intent(Intent.ACTION_VIEW)

        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val providerPath = "${BuildConfig.LIBRARY_PACKAGE_NAME}.FileProvider"
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, providerPath, file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.ms-excel")
        context.startActivity(intent)
    }
}