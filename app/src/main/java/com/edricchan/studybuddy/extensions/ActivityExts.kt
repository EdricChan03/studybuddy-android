package com.edricchan.studybuddy.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Starts an activity ([T]) with the specified [context]
 * @param T The activity to launch
 * @param context The context to be used to launch the activity
 */
inline fun <reified T : Activity> Activity.startActivity(
		context: Context) {
	startActivity(Intent(context, T::class.java))
}
