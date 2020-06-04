package com.hxs.ktutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hxs.ktutil.core.device.ScreenUtil
import com.hxs.ktutil.core.media.VideoUtil
import com.hxs.ktutils.network.KtNetwork

class MainActivity : AppCompatActivity() {

    val url = "http://172.18.11.152:80/data/uploads/videos/202005/202005281702042340.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VideoUtil.videoBytes(url) {
            println(it)
        }
//        Network.method(Method.GET).url(url).addParameter("name", "zhangsan")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ScreenUtil.hasNotchInScreen(this)
    }


}
