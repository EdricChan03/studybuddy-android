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
import com.edricchan.studybuddy.extensions.toDate
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import java.util.*

class UpdateSettingsFragment : MaterialPreferenceFragment(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var updateInfoPreferences: SharedPreferences
    private lateinit var lastUpdatedDate: Date
    private lateinit var lastCheckedForUpdatesDate: Date

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE || key == UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE) {
            when (key) {
                UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE -> {
                    lastUpdatedDate = updateInfoPreferences.getLong(
                        UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
                        0L
                    ).toDate()
                }
                UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE -> {
                    lastCheckedForUpdatesDate = updateInfoPreferences.getLong(
                        UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
                        0L
                    ).toDate()
                }
            }
            updateUpdatesPreferenceSummary()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(LAST_UPDATED_DATE_TAG, lastUpdatedDate.time)
        outState.putLong(LAST_CHECK_FOR_UPDATES_DATE_TAG, lastCheckedForUpdatesDate.time)
        super.onSaveInstanceState(outState)
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_updates, rootKey)

        updateInfoPreferences = requireContext().getSharedPreferences(
            UpdateInfoPrefConstants.FILE_UPDATE_INFO,
            Context.MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(this@UpdateSettingsFragment)
        }

        lastUpdatedDate = savedInstanceState?.getLong(LAST_UPDATED_DATE_TAG)?.toDate()
            ?: updateInfoPreferences.getLong(
                UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
                0L
            ).toDate()
        lastCheckedForUpdatesDate =
            savedInstanceState?.getLong(LAST_CHECK_FOR_UPDATES_DATE_TAG)?.toDate()
                ?: updateInfoPreferences.getLong(
                    UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
                    0L
                ).toDate()

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
            val lastCheckedForUpdates = if (lastCheckedForUpdatesDate.time != 0L) {
                getRelativeDateTimeString(lastCheckedForUpdatesDate)
            } else {
                getString(R.string.pref_updates_summary_never)
            }
            val lastUpdated = if (lastUpdatedDate.time != 0L) {
                getRelativeDateTimeString(lastUpdatedDate)
            } else {
                getString(R.string.pref_updates_summary_never)
            }
            summary = getString(R.string.pref_updates_summary, lastCheckedForUpdates, lastUpdated)
        }
    }

    private fun getRelativeDateTimeString(date: Date): CharSequence =
        DateUtils.getRelativeDateTimeString(
            context,
            date.time,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS,
            0
        )

    companion object {
        // Indicates when the app was last updated
        private const val LAST_UPDATED_DATE_TAG = "lastUpdatedDate"
        // Indicates when the app last checked for updates
        private const val LAST_CHECK_FOR_UPDATES_DATE_TAG = "lastCheckForUpdatesDate"
    }

}
