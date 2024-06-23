package com.edricchan.studybuddy.exts.android

import android.app.Activity
import android.content.Context
import android.content.Intent
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
