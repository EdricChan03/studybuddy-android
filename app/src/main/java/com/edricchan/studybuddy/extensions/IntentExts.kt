package com.edricchan.studybuddy.extensions

import android.content.Context
import android.content.Intent
/**
 * A builder to create an [Intent]
 * @param builderAction Inline functions to be called within the created [Intent]
 * @return The created intent
 */
inline fun buildIntent(builderAction: Intent.() -> Unit) = Intent().apply(builderAction)

/**
 * A builder to create an [Intent]
 * @param context The context of the [Intent]
 * @param builderAction Inline functions to be called within the created [Intent]
 * @param T A valid Java class
 * @return The created intent
 */
inline fun <reified T> buildIntent(
		context: Context,
		builderAction: Intent.() -> Unit
) = Intent(context, T::class.java).apply(builderAction)
