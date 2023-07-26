package com.edricchan.studybuddy.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Starts an activity [T] with the current activity's context
 * @param T The activity to launch
 */
inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(Intent(this, T::class.java))
}

/**
 * Starts an activity [T] with the current activity's context
 * @param T The activity to launch
 * @param intentOptions Intent options
 */
inline fun <reified T : Activity> Activity.startActivity(intentOptions: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java).apply(intentOptions)
    startActivity(intent)
}

/**
 * Starts an activity [T] with the specified [context]
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 */
inline fun <reified T : Activity> Activity.startActivity(context: Context) {
    startActivity(Intent(context, T::class.java))
}

/**
 * Starts an activity [T] with the specified [context]
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param intentOptions Options for the intent
 */
inline fun <reified T : Activity> Activity.startActivity(
    context: Context,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(context, T::class.java).apply(intentOptions)
    startActivity(intent)
}

/**
 * Starts an activity [T] with the current activity's context and [options] to be passed to the intent
 * @param T The activity to launch
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(options: Bundle?) {
    startActivity(Intent(this, T::class.java), options)
}

/**
 * Starts an activity [T] with the current activity's context and [options] to be passed to the intent
 * @param T The activity to launch
 * @param options Options to be passed to the activity
 * @param intentOptions Options to be set on the [Intent]
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(
    options: Bundle?,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(this, T::class.java).apply(intentOptions)
    startActivity(intent, options)
}

/**
 * Starts an activity [T] with the specified [context] and [options] to be passed to the intent
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(context: Context, options: Bundle?) {
    startActivity(Intent(context, T::class.java), options)
}

/**
 * Starts an activity [T] with the specified [context] and [options] to be passed to the intent
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @param intentOptions Options to be set on the [Intent]
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Activity.startActivity(
    context: Context,
    options: Bundle?,
    intentOptions: Intent.() -> Unit
) {
    val intent = Intent(context, T::class.java).apply(intentOptions)
    startActivity(intent, options)
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
    options: Bundle? = null,
    intentOptions: Intent.() -> Unit = {}
) {
    startActivity(Intent(this, T::class.java).apply(intentOptions), options)
}

/**
 * Starts the specified intent with the [Context] receiver, [options]
 * to specify how the activity should be started, and additional [intentOptions]
 * to be specified on the [Intent].
 * @param options Options to specify how the activity should be started.
 * @param intentOptions Options to be applied to the [Intent].
 */
@JvmName("startActivityNotReified")
inline fun Context.startActivity(options: Bundle? = null, intentOptions: Intent.() -> Unit) {
    startActivity(Intent().apply(intentOptions), options)
}
