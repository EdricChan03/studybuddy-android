package com.edricchan.studybuddy.exts.android

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Shows a [Toast] with the specified options.
 * @param message The message to be shown to the user as a [CharSequence]
 * @param duration The length of time to show the toast for
 * @see [Toast.makeText]
 * @see [Toast.setText]
 * @see [Toast.setDuration]
 */
fun Context.showToast(message: CharSequence, duration: Int) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Shows a [Toast] with the specified options.
 * @param messageResId The message to be shown to the user as a string resource ID
 * @param duration The length of time to show the toast for
 * @see [Toast.makeText]
 * @see [Toast.setText]
 * @see [Toast.setDuration]
 */
fun Context.showToast(@StringRes messageResId: Int, duration: Int) {
    Toast.makeText(this, messageResId, duration).show()
}

/**
 * Shows a toast with the specified [toastOptions]. Note that this method will
 * automatically call [Toast.show] for you, so there's no need to pass that with
 * the options.
 * @param toastOptions Options to be passed to the [Toast]
 */
fun Context.showToast(toastOptions: Toast.() -> Unit) {
    Toast(this).apply(toastOptions)
        .show()
}

/**
 * Shows a [Toast] with the specified options.
 * @param message The message to be shown to the user as a [CharSequence]
 * @param duration The length of time to show the toast for
 * @see [Toast.makeText]
 * @see [Toast.setText]
 * @see [Toast.setDuration]
 */
fun Fragment.showToast(message: CharSequence, duration: Int) {
    Toast.makeText(context, message, duration).show()
}

/**
 * Shows a [Toast] with the specified options.
 * @param messageResId The message to be shown to the user as a string resource ID
 * @param duration The length of time to show the toast for
 * @see [Toast.makeText]
 * @see [Toast.setText]
 * @see [Toast.setDuration]
 */
fun Fragment.showToast(@StringRes messageResId: Int, duration: Int) {
    Toast.makeText(context, messageResId, duration).show()
}

/**
 * Shows a toast with the specified [toastOptions]. Note that this method will
 * automatically call [Toast.show] for you, so there's no need to pass that with
 * the options.
 * @param toastOptions Options to be passed to the [Toast]
 */
fun Fragment.showToast(toastOptions: Toast.() -> Unit) {
    Toast(context).apply(toastOptions)
        .show()
}
