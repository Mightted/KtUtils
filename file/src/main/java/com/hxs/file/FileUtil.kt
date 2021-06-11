package com.hxs.file

import android.content.Context
import android.os.Environment
import java.io.File

object FileUtil {

    // /data/user/0/com.hxs.ktutils/app_xxx
    fun getAppRootPath(context: Context, file_suffix: String):File {
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
}