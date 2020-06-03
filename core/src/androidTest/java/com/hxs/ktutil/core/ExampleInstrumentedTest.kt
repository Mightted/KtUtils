package com.hxs.ktutil.core

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hxs.ktutil.core.media.VideoUtil

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.hxs.ktutil.core.test", appContext.packageName)
    }


    @Test
    fun testVideoByte() {
//        val length = VideoUtil.videoBytes("http://172.18.11.152:80/data/uploads/videos/202005/202005281702042340.mp4")
//        assertNotEquals(0, length)
    }
}
