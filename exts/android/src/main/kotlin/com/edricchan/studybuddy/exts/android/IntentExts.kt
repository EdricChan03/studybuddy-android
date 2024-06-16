package com.edricchan.studybuddy.exts.android

import android.content.Context
import android.content.Intent

/**
 * A builder to create an [Intent]
 * @param builderAction Inline functions to be called within the created [Intent]
 * @return The created intent
 */
inline fun buildIntent(builderAction: Intent.() -> Unit) = Intent().apply(builderAction)

/**
 * A builder to create an [Intent] with the specified [action] pre-filled.
 * @param action The [action][Intent.setAction] to be set for the [Intent].
 * @param builderAction Inline functions to be called within the created [Intent].
 */
inline fun buildIntent(action: String, builderAction: Intent.() -> Unit) =
    Intent(action).apply(builderAction)

/**
 * A builder to create a chooser [Intent].
 * @param title The title to be shown in the chooser.
 * @param builderAction Inline functions to be called within the created [Intent]
 * @return The created intent
 */
inline fun buildChooserIntent(
    title: String, builderAction: Intent.() -> Unit
) = Intent.createChooser(
    buildIntent(builderAction),
    title
)

/**
 * Starts a chooser [Intent] given the arguments.
 * @param title The title to be shown in the chooser.
 * @param builderAction Inline functions to be called within the created [Intent]
 */
inline fun Context.startChooser(
    title: String, builderAction: Intent.() -> Unit
) = startActivity(buildChooserIntent(title, builderAction))

/**
 * A builder to create an [Intent]
 * @param context The context of the [Intent]
 * @param builderAction Inline functions to be called within the created [Intent]
 * @param T A valid Java class
 * @return The created intent
 */
inline fun <reified T> buildIntent(
    context: Context?,
    builderAction: Intent.() -> Unit = {}
) = Intent(context, T::class.java).apply(builderAction)
