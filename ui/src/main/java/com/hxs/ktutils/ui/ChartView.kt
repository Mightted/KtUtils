package com.hxs.ktutils.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class ChartView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val attr = ChartItemAttr(resources.displayMetrics.density)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


    }

    inner class ChartItemAttr(private val density: Float) {
        val width: Int = 16
            get() = field * density.toInt()

        var color: Int = Color.BLUE


    }

}