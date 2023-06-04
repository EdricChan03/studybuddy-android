package com.edricchan.studybuddy.extensions.context

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Starts an activity with the given [intentOptions] to be used.
 * @param intentOptions Options to be set on the [Intent] to launch.
 */
inline fun Context.startActivity(intentOptions: Intent.() -> Unit) {
    startActivity(Intent().apply(intentOptions))
}

/**
 * Starts an activity with the given [intentOptions] to be used. The [action]
 * and [data] parameters are passed to the [Intent] constructor that accepts
 * both of them.
 * @param action See [Intent.setAction]
 * @param data See [Intent.setData]
 * @param intentOptions Options to be set on the [Intent] to launch.
 */
inline fun Context.startActivity(
    action: String,
    data: Uri? = null,
    intentOptions: Intent.() -> Unit = {}
) {
    startActivity(Intent(action, data).apply(intentOptions))
}
