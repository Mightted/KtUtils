package com.hxs.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hxs.ktutil.core.app.BitmapUtil
import kotlinx.android.synthetic.main.activity_test_main.*

class TestMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_main)

        val view = layoutInflater.inflate(R.layout.layout_bitmap_view, null)
        testImg.setImageBitmap(BitmapUtil.viewToBitmap(this, view))
    }
}