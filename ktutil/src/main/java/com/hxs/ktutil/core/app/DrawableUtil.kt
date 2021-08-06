package com.hxs.ktutil.core.app

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Time: 2020/6/14
 * Author: Mightted
 * Description:
 */
object DrawableUtil {

    /**
     * 修改ImageView组件的底色
     */
    fun setColorFilter(view: ImageView, color: Int) {
        view.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            color, BlendModeCompat.SRC_ATOP
        )
    }

    /**
     *
     * 修改drawable的底色，使用了这个资源的组件也会生效
    */
    fun setColorFilter(drawable: Drawable, color: Int) {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color))
    }
}