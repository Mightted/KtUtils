package com.hxs.ktutil.core.misc

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.hxs.ktutil.core.app.AppUtil
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


    fun file2Uri(file: File, providerPath: String?, context: Context? = AppUtil.context()): Uri? {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (providerPath == null || context == null) {
                return null
            }
            FileProvider.getUriForFile(context, providerPath, file)
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            Uri.fromFile(file)
        }
    }


}