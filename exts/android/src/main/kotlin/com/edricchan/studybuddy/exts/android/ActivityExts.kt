package com.edricchan.studybuddy.exts.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

/**
 * Starts the specified [activity][T] with the [Context] receiver and
 * additional [intentOptions] to be specified on the [Intent].
 * @param T The activity to launch.
 * @param intentOptions [Intent] options to be set.
 */
inline fun <reified T : Activity> Context.startActivity(intentOptions: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(intentOptions))
}

/**
 * Starts the specified [activity][T] with the [Context] receiver, [options]
 * to specify how the activity should be started, and additional [intentOptions]
 * to be specified on the [Intent].
 * @param T The activity to launch.
 * @param options Options to specify how the activity should be started.
 * @param intentOptions Options to be applied to the [Intent].
 */
inline fun <reified T : Activity> Context.startActivity(
    options: Bundle?,
    intentOptions: Intent.() -> Unit = {}
) {
    startActivity(Intent(this, T::class.java).apply(intentOptions), options)
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

/**
 * Starts an activity with the given [intentOptions] to be used.
 * @param intentOptions Options to be set on the [Intent] to launch.
 */
@JvmName("startActivityNoClass")
inline fun Context.startActivity(intentOptions: Intent.() -> Unit) {
    startActivity(Intent().apply(intentOptions))
}
