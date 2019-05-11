package com.edricchan.studybuddy.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Starts an activity [T] with the fragment's context
 * @param T The activity to launch
 */
inline fun <reified T : Activity> Fragment.startActivity() {
	startActivity(Intent(context, T::class.java))
}

/**
 * Starts an activity [T] with the specified [context]
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 */
inline fun <reified T : Activity> Fragment.startActivity(context: Context) {
	startActivity(Intent(context, T::class.java))
}

/**
 * Starts an activity [T] with the fragment's context and [options] to be passed to the intent
 * @param T The activity to launch
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Fragment.startActivity(options: Bundle?) {
	startActivity(Intent(context, T::class.java), options)
}

/**
 * Starts an activity [T] with the specified [context] and [options] to be passed to the intent
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Fragment.startActivity(context: Context, options: Bundle?) {
	startActivity(Intent(context, T::class.java), options)
}

/**
 * Starts an activity [T] with the fragment's context for [requestCode]
 * @param T The activity to launch
 * @param requestCode The request code returned by the activity once finished
 * @see Fragment.onActivityResult
 */
inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int) {
	startActivityForResult(Intent(context, T::class.java), requestCode)
}

/**
 * Starts an activity [T] with the specified [context] and return a [requestCode] when the activity is finished
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param requestCode The request code returned by the activity once finished
 * @see Fragment.onActivityResult
 */
inline fun <reified T : Activity> Fragment.startActivityForResult(context: Context, requestCode: Int) {
	startActivityForResult(Intent(context, T::class.java), requestCode)
}

/**
 * Starts an activity [T] with the fragment's context, passes [options] to the intent and return the [requestCode]
 * when the activity is finished
 * @param T The activity to launch
 * @param requestCode The request code returned by the activity once finished
 * @param options Options to be passed to the activity
 * @see Fragment.onActivityResult
 * @see Intent.getExtras
 */
inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, options: Bundle?) {
	startActivityForResult(Intent(context, T::class.java), requestCode, options)
}

/**
 * Starts an activity [T] with the specified [context], passes [options] to the intent and return the [requestCode]
 * when the activity is finished
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 * @param options Options to be passed to the activity
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Fragment.startActivityForResult(context: Context, requestCode: Int, options: Bundle?) {
	startActivityForResult(Intent(context, T::class.java), requestCode, options)
}