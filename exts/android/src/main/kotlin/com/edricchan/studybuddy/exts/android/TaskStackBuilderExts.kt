package com.edricchan.studybuddy.exts.android

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.TaskStackBuilder

fun Context.createPendingIntent(
    requestCode: Int,
    flags: Int,
    builder: TaskStackBuilder.() -> Unit
): PendingIntent? = TaskStackBuilder.create(this).run {
    builder()

    getPendingIntent(requestCode, flags)
}
