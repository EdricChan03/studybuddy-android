package com.edricchan.studybuddy.extensions

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/** Retrieves an instance of [WorkManager] given the receiver [Context]. */
val Context.workManager get() = WorkManager.getInstance(this)

/**
 * Creates a periodic work request using the given parameters.
 * @param repeatInterval The repeat interval in [repeatIntervalTimeUnit] units.
 * @param repeatIntervalTimeUnit The [TimeUnit] for [repeatInterval].
 * @param init Configuration to be passed to [PeriodicWorkRequest.Builder].
 */
inline fun <reified W : ListenableWorker> periodicWorkRequest(
    repeatInterval: Long, repeatIntervalTimeUnit: TimeUnit,
    init: PeriodicWorkRequest.Builder.() -> Unit
) = PeriodicWorkRequestBuilder<W>(
    repeatInterval, repeatIntervalTimeUnit
).apply(init).build()
