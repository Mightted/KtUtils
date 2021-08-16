package com.hxs.ktutil.core.misc

import android.content.Context
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import com.hxs.ktutil.core.app.AppUtil
import com.hxs.ktutil.core.app.ToastUtil


class LogUtil private constructor() {

    private var enabledToastLog: Boolean = false


    companion object {
        private const val TAG = "Soniko♪"
        private val instance: LogUtil by lazy {
            LogUtil()
        }


        fun init(context: Context, logSavedPath: String = "") {
            instance.init(context, logSavedPath)
        }

        fun enabledToastLog(enable: Boolean) {
            instance.enabledToastLog = enable
        }

        fun debug(message: String, isShow: Boolean = AppUtil.isDebug()) {
            debug(message, isShow)
        }

        fun debug(tag: String = TAG, msg: String, isShow: Boolean = AppUtil.isDebug()) {
            instance.debug(tag, msg, isShow)
        }

        fun error(message: String) {
            instance.error(message)
        }

        fun error(e: Throwable) {
            instance.error(e)
        }


    }


    private fun init(context: Context, logSavedPath: String = "") {
//        XLog.init(if (AppUtil.isDebug()) LogLevel.ALL else LogLevel.NONE)
//        Logger.addLogAdapter(object : DiskLogAdapter(PrettyFormatStrategy.newBuilder().tag(TAG).build()) {
//            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return true
//            }
//        })

        val config = LogConfiguration.Builder()
            .logLevel(if (AppUtil.isDebug()) LogLevel.ALL else LogLevel.NONE)
            .tag(TAG)                                         // 指定 TAG，默认为 "X-LOG"
            .st(2)                                                 // 允许打印深度为2的调用栈信息，默认禁止
//                .b()                                                   // 允许打印日志边框，默认禁止
            .build()


        val filePrinter = if (logSavedPath.isEmpty()) {
            FilePrinter                      // 打印日志到文件的打印器
                .Builder("${context.getExternalFilesDir(null)?.path}/xlog/")                              // 指定保存日志文件的路径
                .fileNameGenerator(DateFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .backupStrategy(NeverBackupStrategy())         // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                .build()
        } else {
            null
        }

        XLog.init(config, AndroidPrinter(), filePrinter)

    }

    fun debug(message: String, isShow: Boolean = AppUtil.isDebug()) {
        if (isShow) {
            XLog.d(message)
            if (enabledToastLog) {
                ToastUtil.toast(message)
            }
        }
    }


    fun debug(tag: String = TAG, msg: String, isShow: Boolean = AppUtil.isDebug()) {
        if (isShow) {
            XLog.tag(tag).d(tag, msg)
        }
    }

    fun error(message: String) {
        XLog.e(message)
    }

    fun error(e: Throwable) {
        e.message?.let { XLog.e(it) }
    }

    fun exception(e: Exception) {
        e.message?.let { XLog.e(it) }
    }
}
