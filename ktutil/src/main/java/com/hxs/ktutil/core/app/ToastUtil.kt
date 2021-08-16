package com.hxs.ktutil.core.app

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

object ToastUtil {

    fun toast(
        str: String,
        context: Context? = AppUtil.context(),
        duration: Int = Toast.LENGTH_SHORT
    ) {
        assert(context != null)
        Toast.makeText(context, str, duration).show()
    }


    fun toast(
        @StringRes strId: Int,
        context: Context? = AppUtil.context(),
        duration: Int = Toast.LENGTH_SHORT
    ) {
        assert(context != null)
        Toast.makeText(context, strId, duration).show()
    }

    fun snack(
        view: View,
        str: String,
        actionStr: String,
        action: (() -> Unit)? = null,
        duration: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(view, str, duration).run {
            if (action != null) {
                setAction(actionStr) {
                    action.invoke()
                }
            }
            show()
        }
    }


}