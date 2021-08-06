package com.hxs.ktutils

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hxs.ktutil.core.device.HardwareUtil
import com.hxs.ktutil.core.device.WhiteListUtil
import com.hxs.ktutils.core.misc.FileUtil
import com.hxs.test.SaveExcelActivity
import com.hxs.test.TestMainActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

//    val url = "http://172.18.11.152:80/data/uploads/videos/202005/202005281702042340.mp4"

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        VideoUtil.videoBytes(url) {
//            println(it)
//        }

        btnPress.setOnClickListener {

            startActivity(Intent(this, SaveExcelActivity::class.java))

//            CoroutineScope(Dispatchers.IO).launch {
//                val request =
//                    KtRequest().url("http://172.18.11.152:81/?url=admin/goods/goods_video")
//                        .addParameter("act", "upload")
//                        .addParameter("goods_id", "1718")
//                        .addFileParameter(
//                            "video/mp4",
//                            File("/storage/emulated/0/Mob/cn.sharesdk.demo/cache/videos/e613d3d6ba6c1524b4abab146249dac0.mp4"),
//                            "goods_video",
//                            "video.mp4"
//                        )
//                        .setOnProgressChangeListener { progress ->
//                            println("hxs:进度为$progress")
//                        }
//                KtNetwork.postRequest(request)
//            }
        }

        btnRequestBatteryOpt.setOnClickListener {
            if (HardwareUtil.higherBuildVersion(Build.VERSION_CODES.M)) {
                if (!WhiteListUtil.isIgnoringBatteryOptimizations(this)) {
                    WhiteListUtil.requestIgnoreBatteryOptimizations(this)
                }
            }
        }

        btnJumAppPage.setOnClickListener {
            WhiteListUtil.goWhiteListSetting(this)
        }

        FileUtil.getFileList(this)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        ScreenUtil.hasNotchInScreen(this)
    }


}
