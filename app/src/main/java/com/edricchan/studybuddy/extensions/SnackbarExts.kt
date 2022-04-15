package com.edricchan.studybuddy.extensions

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

/**
 * Creates a [Snackbar] given the specified options.
 * @param view The view where the snackbar will show at.
 * @param text The text to be shown as a [CharSequence].
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 * @return The created [Snackbar].
 */
fun createSnackbar(
    view: View, text: CharSequence,
    @BaseTransientBottomBar.Duration duration: Int,
    snackbarOptions: Snackbar.() -> Unit
) = Snackbar.make(view, text, duration).apply(snackbarOptions)

/**
 * Creates a [Snackbar] given the specified options.
 * @param view The view where the snackbar will show at.
 * @param textResId The text to be shown as a string resource.
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 * @return The created [Snackbar].
 */
fun createSnackbar(
    view: View, @StringRes textResId: Int,
    @BaseTransientBottomBar.Duration duration: Int,
    snackbarOptions: Snackbar.() -> Unit
) = Snackbar.make(view, textResId, duration).apply(snackbarOptions)

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param text The text to be shown, as a [CharSequence].
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun showSnackbar(
    view: View, text: CharSequence,
    @BaseTransientBottomBar.Duration duration: Int,
    snackbarOptions: Snackbar.() -> Unit = {}
) {
    createSnackbar(view, text, duration, snackbarOptions).show()
}

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param textResId The text to be shown, as a string resource.
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun showSnackbar(
    view: View, @StringRes textResId: Int,
    @BaseTransientBottomBar.Duration duration: Int,
    snackbarOptions: Snackbar.() -> Unit = {}
) {
    createSnackbar(view, textResId, duration, snackbarOptions).show()
}
