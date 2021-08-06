package com.hxs.ktutil.core.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent

class ContextExt {
}

inline fun Context.newIntent(clazz: Class<out Activity>): Intent {
    val intent = Intent(this, clazz)
    if (this is Application) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    return intent
}