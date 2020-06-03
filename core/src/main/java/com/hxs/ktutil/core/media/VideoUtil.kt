package com.hxs.ktutil.core.media

import android.webkit.URLUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object VideoUtil {

    fun videoBytes(url: String, callback: (Int) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val length = videoBytes(url)
            callback(length)
        }
    }

    /**
     * 获取视频字节大小，其他文件也适用
     */
    suspend fun videoBytes(url: String) = withContext(Dispatchers.IO) {

        var length = 0
        if (URLUtil.isNetworkUrl(url)) {
            val connection = URL(url).openConnection()
            connection.connect()
            if ((connection as HttpURLConnection).responseCode == 200) {
                length = connection.contentLength
            }
        }
        length
    }


}