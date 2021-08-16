package com.hxs.ktutil

import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

class KtUtilEntrance private constructor() {

    var context: WeakReference<Context>? = null

    companion object {
        private val instance by lazy {
            KtUtilEntrance()
        }

        fun init(application: Application) {
            instance.run {
                if (context?.get() != null) {
                    throw RuntimeException("KtUtil not allowed init twice!!")
                }
                context = WeakReference(application)
            }
        }

        internal fun context() = instance.context?.get()

    }


}