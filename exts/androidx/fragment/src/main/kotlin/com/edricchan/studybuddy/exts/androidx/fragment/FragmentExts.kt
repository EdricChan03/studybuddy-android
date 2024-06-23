package com.edricchan.studybuddy.exts.androidx.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialFadeThrough

/**
 * Starts an activity [T] with the fragment's context
 * @param T The activity to launch
 */
inline fun <reified T : Activity> Fragment.startActivity(intentOptions: Intent.() -> Unit = {}) {
    startActivity(Intent(context, T::class.java).apply(intentOptions))
}

/**
 * Starts an activity [T] with the fragment's context and [options] to be passed to the intent
 * @param T The activity to launch
 * @param options Options to be passed to the activity
 * @param intentOptions Options to be set on the [Intent]
 * @see Intent.getExtras This can be used in the activity launched to retrieve the [options] specified
 */
inline fun <reified T : Activity> Fragment.startActivity(
    options: Bundle?,
    intentOptions: Intent.() -> Unit = {}
) {
    val intent = Intent(context, T::class.java).apply(intentOptions)
    startActivity(intent, options)
}

/**
 * Replaces the fragment at the given [viewId] with the specified [fragment].
 * Optionally, the ability to add the fragment to the back stack can be specified,
 * as well as whether to have animations, if any.
 * @param viewId The ID of the view to replace
 * @param fragment The fragment to replace the view with
 * @param addToBackStack Whether to add the fragment to the back stack
 * @param hasAnimations Whether to have animations
 * @return True if the fragment was replaced, false otherwise
 */
fun FragmentActivity.replaceFragment(
    @IdRes viewId: Int, fragment: Fragment,
    addToBackStack: Boolean,
    hasAnimations: Boolean = true
) =
    if (supportFragmentManager.findFragmentById(viewId) !== fragment) {
        with(supportFragmentManager) {
            // Set exit transition on the latest fragment
            // (lastOrNull is used in the event that the list of fragments is empty, which can
            // happen if there are no initial fragments)
            if (hasAnimations) fragments.lastOrNull()?.exitTransition = MaterialFadeThrough()
            commit {
                if (hasAnimations) fragment.enterTransition = MaterialFadeThrough()
                replace(viewId, fragment)
                if (addToBackStack) addToBackStack(null)
            }
        }
        // Indicate that the fragment replacement has been done.
        true
    }
    // Return false if there's already an existing fragment.
    else false
