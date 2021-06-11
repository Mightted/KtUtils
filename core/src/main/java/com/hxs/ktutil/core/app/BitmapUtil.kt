package com.hxs.ktutil.core.app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup

object BitmapUtil {

    fun viewToBitmap(context: Context?, addViewContent: View?): Bitmap? {

        if (context == null || addViewContent == null) {
            return null
        }
        val displayMetrics = context.resources.displayMetrics
        addViewContent.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addViewContent.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        addViewContent.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
//        addViewContent.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            addViewContent.measuredWidth,
            addViewContent.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        addViewContent.draw(canvas)
        return bitmap
    }

    fun getBitmapFromView(view: View?): Bitmap? {
        if (view == null) {
            return null
        }
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}