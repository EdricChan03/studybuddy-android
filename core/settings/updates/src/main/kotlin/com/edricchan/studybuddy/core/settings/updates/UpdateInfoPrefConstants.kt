package com.edricchan.studybuddy.core.settings.updates

/**
 * Constants to be used for shared preferences regarding update info
 */
object UpdateInfoPrefConstants {
    /**
     * File to be used for storing information about updates
     */
    const val FILE_UPDATE_INFO = "update_info"

    /**
     * Indicates when the app was last updated via the Update UI in Settings > Versions & About
     *
     * This key is stored as a [Long] representation of a date (Use [java.util.Date.getTime] to convert a [java.util.Date] to a [Long])
     */
    const val PREF_LAST_UPDATED_DATE = "last_updated_date"

    /**
     * Indicates when the app last checked for updates
     *
     * This key is stored as a [Long] representation of a date (Use [java.util.Date.getTime] to convert a [java.util.Date] to a [Long])
     */
    const val PREF_LAST_CHECKED_FOR_UPDATES_DATE = "last_checked_for_updates_date"
}
