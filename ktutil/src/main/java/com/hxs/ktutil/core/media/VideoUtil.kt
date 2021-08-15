package com.hxs.ktutil.core.media

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.webkit.URLUtil
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


object VideoUtil {

    fun videoBytes(url: String, callback: (Int) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val length = videoBytes(url)
            callback(length)
        }
    }

    /**
     * 获取视频字节大小，其他文件也适用
     */
    @WorkerThread
    private fun videoBytes(url: String): Int {

        var length = 0
        if (URLUtil.isNetworkUrl(url)) {
            val connection = URL(url).openConnection()
            try {
                connection.connect()
                if ((connection as HttpURLConnection).responseCode == 200) {
                    length = connection.contentLength
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
        return length
    }


    // 获取视频第一帧作为缩略图
    fun getVideoThumbnail(url: String): Bitmap? {
        val media = MediaMetadataRetriever()

        if (URLUtil.isHttpUrl(url)) {
            media.setDataSource(url, HashMap<String, String>())
        } else {
            media.setDataSource(url)
        }
        return media.frameAtTime
    }


}