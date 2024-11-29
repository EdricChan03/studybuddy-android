package com.edricchan.studybuddy.core.settings.updates

object UpdateInfoPrefConstants {
    /** File to be used for storing information about updates. */
    const val FILE_UPDATE_INFO = "update_info"

    /**
     * Indicates when the app was last updated.
     *
     * This key's value is stored as a [Long] representation of a date in milli-seconds.
     */
    const val PREF_LAST_UPDATED_DATE = "last_updated_date"

    /**
     * Indicates when the app last checked for updates
     *
     * This key's value is stored as a [Long] representation of a date in milli-seconds.
     */
    const val PREF_LAST_CHECKED_FOR_UPDATES_DATE = "last_checked_for_updates_date"
}
