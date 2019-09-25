package com.edricchan.studybuddy.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param text The text to be shown, as a [CharSequence].
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun Context.showSnackbar(view: View, text: CharSequence,
                         @BaseTransientBottomBar.Duration duration: Int,
                         snackbarOptions: Snackbar.() -> Unit = {}) {
	Snackbar.make(view, text, duration)
			.apply(snackbarOptions)
			.show()
}

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param textResId The text to be shown, as a string resource.
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun Context.showSnackbar(view: View, @StringRes textResId: Int,
                         @BaseTransientBottomBar.Duration duration: Int,
                         snackbarOptions: Snackbar.() -> Unit = {}) {
	Snackbar.make(view, textResId, duration)
			.apply(snackbarOptions)
			.show()
}

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param text The text to be shown, as a [CharSequence].
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun Activity.showSnackbar(view: View, text: CharSequence,
                          @BaseTransientBottomBar.Duration duration: Int,
                          snackbarOptions: Snackbar.() -> Unit = {}) {
	Snackbar.make(view, text, duration)
			.apply(snackbarOptions)
			.show()
}

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param textResId The text to be shown, as a string resource.
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun Activity.showSnackbar(view: View, @StringRes textResId: Int,
                         @BaseTransientBottomBar.Duration duration: Int,
                          snackbarOptions: Snackbar.() -> Unit = {}) {
	Snackbar.make(view, textResId, duration)
			.apply(snackbarOptions)
			.show()
}

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param text The text to be shown, as a [CharSequence].
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun Fragment.showSnackbar(view: View, text: CharSequence,
                          @BaseTransientBottomBar.Duration duration: Int,
                          snackbarOptions: Snackbar.() -> Unit = {}) {
	Snackbar.make(view, text, duration)
			.apply(snackbarOptions)
			.show()
}

/**
 * Shows a [Snackbar] with the specified options.
 * @param view The view where the snackbar will show at.
 * @param textResId The text to be shown, as a string resource.
 * @param duration The duration to show the snackbar for.
 * @param snackbarOptions Additional options to be passed to the [Snackbar].
 */
fun Fragment.showSnackbar(view: View, @StringRes textResId: Int,
                         @BaseTransientBottomBar.Duration duration: Int,
                          snackbarOptions: Snackbar.() -> Unit = {}) {
	Snackbar.make(view, textResId, duration)
			.apply(snackbarOptions)
			.show()
}
