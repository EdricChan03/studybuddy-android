package com.edricchan.studybuddy.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.Operation
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.extensions.periodicWorkRequest
import com.edricchan.studybuddy.extensions.workManager
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.datetime.toDuration
import com.edricchan.studybuddy.workers.CheckForUpdatesWorker
import java.time.Duration
import java.time.temporal.ChronoUnit

/** Constant used to uniquely identify the [update worker][CheckForUpdatesWorker]. */
const val UPDATE_WORK_NAME = "workUpdater"

/**
 * Retrieves the JSON update URL.
 * Used for [SharedUtils.checkForUpdates]
 * @param forceDebugUrl Whether to return the debug URL even if [isDevMode] is `true`.
 * @return The JSON update URL as a [String]
 */
fun Context.getUpdateJsonUrl(forceDebugUrl: Boolean = false): String {
    return if (isDevMode() || forceDebugUrl) {
        val mPrefs = defaultSharedPreferences
        if (mPrefs.getBoolean(Constants.debugUseTestingJsonUrl, true)) {
            mPrefs.getString(
                Constants.debugSetCustomJsonUrl,
                getString(R.string.update_json_testing_url)
            )!!
        } else {
            getString(R.string.update_json_release_url)
        }
    } else {
        getString(R.string.update_json_release_url)
    }
}

/**
 * Enqueues a unique worker that [checks for updates][CheckForUpdatesWorker].
 * This method retrieves the values from [android.content.SharedPreferences]
 * and uses their values as worker constraints.
 * @return Information on the enqueued worker.
 */
fun Context.enqueueUniqueCheckForUpdatesWorker(): Operation? {
    val sharedPreferences = defaultSharedPreferences
    val defaultInterval =
        resources.getInteger(R.integer.pref_check_for_updates_frequency_default_value)
    val repeatInterval = sharedPreferences.getString(
        Constants.prefUpdatesFrequency,
        defaultInterval.toString()
    )!!.toLong()
    val isMetered =
        sharedPreferences.getBoolean(Constants.prefUpdatesDownloadOverMetered, false)
    val requiresCharging =
        sharedPreferences.getBoolean(Constants.prefUpdatesDownloadOnlyWhenCharging, false)

    return enqueueUniqueCheckForUpdatesWorker(
        repeatInterval.toDuration(ChronoUnit.HOURS),
        isMetered,
        requiresCharging
    )
}

/**
 * Enqueues a unique worker that [checks for updates][CheckForUpdatesWorker].
 * @param repeatInterval The repeat interval to use.
 * @param isMetered Whether to check for updates on a metered connection.
 * @param requiresCharging Whether the device must be charging.
 * @return Information on the enqueued worker.
 */
fun Context.enqueueUniqueCheckForUpdatesWorker(
    repeatInterval: Duration = Duration.ZERO,
    isMetered: Boolean,
    requiresCharging: Boolean
): Operation? {
    // Check if the update frequency is set to manual/never
    if (repeatInterval <= Duration.ZERO) return null

    val networkType =
        if (isMetered) androidx.work.NetworkType.METERED else androidx.work.NetworkType.UNMETERED

    val constraints = Constraints(
        requiredNetworkType = networkType,
        requiresCharging = requiresCharging
    )

    val checkForUpdatesWorkerRequest = periodicWorkRequest<CheckForUpdatesWorker>(
        repeatInterval
    ) {
        setConstraints(constraints)
    }

    return workManager.enqueueUniquePeriodicWork(
        UPDATE_WORK_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        checkForUpdatesWorkerRequest
    )
}
