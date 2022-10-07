package com.edricchan.studybuddy.extensions

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration

/** Retrieves an instance of [WorkManager] given the receiver [Context]. */
val Context.workManager get() = WorkManager.getInstance(this)

/**
 * Creates a periodic work request using the given parameters.
 * @param repeatInterval The repeat interval.
 * @param init Configuration to be passed to [PeriodicWorkRequest.Builder].
 */
inline fun <reified W : ListenableWorker> periodicWorkRequest(
    repeatInterval: Duration, init: PeriodicWorkRequest.Builder.() -> Unit
) = PeriodicWorkRequestBuilder<W>(repeatInterval).apply(init).build()
