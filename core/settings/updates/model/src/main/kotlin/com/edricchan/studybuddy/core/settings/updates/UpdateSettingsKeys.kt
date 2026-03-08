package com.edricchan.studybuddy.core.settings.updates

/**
 * Key for the updates frequency preference.
 *
 * The preference's value is the frequency to check for updates, in
 * hours.
 */
const val keyPrefUpdatesFrequency = "pref_updates_frequency"

/**
 * Key for the download over metered preference.
 *
 * The preference's value is a [Boolean], indicating whether updates
 * can be downloaded over a metered network.
 */
const val keyPrefCanDownloadMetered = "pref_updates_download_over_metered"

/**
 * Key for the download only when charging preference.
 *
 * The preference's value is a [Boolean], indicating whether updates
 * can only be downloaded when the device is charging.
 */
const val keyPrefOnlyDownloadCharging = "pref_updates_download_only_when_charging"
