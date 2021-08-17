package com.hxs.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.hxs.ktutil.core.app.BitmapUtil
import com.hxs.test.coroutines.CoroutinesTester
import kotlinx.android.synthetic.main.activity_test_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)
//        testMain()
//        val view = layoutInflater.inflate(R.layout.layout_bitmap_view, null)
//        testImg.setImageBitmap(BitmapUtil.viewToBitmap(this, view))
    }


    fun testMain() = lifecycleScope.launch(Dispatchers.Main) {
        val ret1 = CoroutinesTester.mainTest1()
        val ret2 = CoroutinesTester.mainTest2()
        println("$ret1 : $ret2")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}