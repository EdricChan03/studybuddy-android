package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateUtils
import androidx.preference.Preference
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.UpdateInfoPrefConstants
import com.edricchan.studybuddy.extensions.getSerializableCompat
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import java.time.Instant

class UpdateSettingsFragment : MaterialPreferenceFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var updateInfoPreferences: SharedPreferences
    private var lastUpdatedInstant: Instant? = null
    private var lastCheckedForUpdatesInstant: Instant? = null

    private fun setLastCheckedForUpdates(lastCheckedForUpdatesMs: Long) {
        lastCheckedForUpdatesInstant =
            lastCheckedForUpdatesMs.takeIf { it <= DEFAULT_INSTANT }
                ?.let { Instant.ofEpochMilli(it) }
    }

    private fun setLastUpdated(lastUpdatedMs: Long) {
        lastUpdatedInstant =
            lastUpdatedMs.takeIf { it <= DEFAULT_INSTANT }
                ?.let { Instant.ofEpochMilli(it) }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE || key == UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE) {
            when (key) {
                UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE -> {
                    setLastCheckedForUpdates(
                        updateInfoPreferences.getLong(
                            UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
                            DEFAULT_INSTANT
                        )
                    )
                }

                UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE -> {
                    setLastUpdated(
                        updateInfoPreferences.getLong(
                            UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
                            DEFAULT_INSTANT
                        )
                    )
                }
            }
            updateUpdatesPreferenceSummary()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(
            LAST_CHECK_FOR_UPDATES_DATE_TAG,
            lastCheckedForUpdatesInstant
        )
        outState.putSerializable(LAST_UPDATED_DATE_TAG, lastUpdatedInstant)
        super.onSaveInstanceState(outState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_updates, rootKey)

        updateInfoPreferences = requireContext().getSharedPreferences(
            UpdateInfoPrefConstants.FILE_UPDATE_INFO,
            Context.MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(this@UpdateSettingsFragment)
        }

        savedInstanceState?.run {
            lastCheckedForUpdatesInstant = getSerializableCompat(
                LAST_CHECK_FOR_UPDATES_DATE_TAG
            )
            lastUpdatedInstant = getSerializableCompat(
                LAST_UPDATED_DATE_TAG
            )
        } ?: run {
            setLastCheckedForUpdates(
                updateInfoPreferences
                    .getLong(
                        UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
                        DEFAULT_INSTANT
                    )
            )
            setLastUpdated(
                updateInfoPreferences
                    .getLong(
                        UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
                        DEFAULT_INSTANT
                    )
            )
        }

        findPreference<Preference>(Constants.prefUpdates)?.intent =
            Intent(context, UpdatesActivity::class.java)

        updateUpdatesPreferenceSummary()
    }

    override fun onPause() {
        super.onPause()
        updateInfoPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        updateInfoPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun updateUpdatesPreferenceSummary() {
        findPreference<Preference>(Constants.prefUpdates)?.apply {
            val lastCheckedForUpdates =
                lastCheckedForUpdatesInstant?.let { getRelativeDateTimeString(it) }
                    ?: getString(R.string.pref_updates_summary_never)
            val lastUpdated = lastUpdatedInstant?.let { getRelativeDateTimeString(it) }
                ?: getString(R.string.pref_updates_summary_never)
            summary = getString(R.string.pref_updates_summary, lastCheckedForUpdates, lastUpdated)
        }
    }

    private fun getRelativeDateTimeString(instant: Instant): CharSequence =
        DateUtils.getRelativeDateTimeString(
            context,
            instant.toEpochMilli(),
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS,
            0
        )

    companion object {
        // Indicates when the app was last updated
        private const val LAST_UPDATED_DATE_TAG = "lastUpdatedDate"

        // Indicates when the app last checked for updates
        private const val LAST_CHECK_FOR_UPDATES_DATE_TAG = "lastCheckForUpdatesDate"

        private const val DEFAULT_INSTANT = -1L
    }

}
