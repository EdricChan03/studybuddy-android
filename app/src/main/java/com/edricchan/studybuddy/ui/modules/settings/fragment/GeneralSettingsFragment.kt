package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.Constants
import com.edricchan.studybuddy.utils.SharedUtils

class GeneralSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

	private var prefDayNightPermInfo: Preference? = null
	private var prefDayNightGrantPerm: Preference? = null

	override fun onResume() {
		super.onResume()
		preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
	}

	override fun onPause() {
		super.onPause()
		preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		Log.d(TAG, "Shared preference changed at key $key")
		if (key == Constants.prefDarkTheme) {
			updateDayNightPermVisibility(sharedPreferences)
		}
	}
	// See https://stackoverflow.com/a/25532722/6782707
	/*override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity)
	}*/

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_general, rootKey)
		prefDayNightGrantPerm = findPreference(Constants.prefDaynightGrantPerm)
		prefDayNightPermInfo = findPreference(Constants.prefDayNightPermInfo)
		updateDayNightPermVisibility(preferenceManager.sharedPreferences)
		if (isLocationPermGranted()) {
			// Hide permission-related Preferences as the permission is already granted
			setDayNightPermVisibility(false)
		} else {
			prefDayNightGrantPerm?.setOnPreferenceClickListener {
				requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0)
				true
			}
		}

	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if (requestCode == 0) {
			for (i in permissions.indices) {
				if (permissions[i] == Manifest.permission.ACCESS_COARSE_LOCATION) {
					// Check if the permission is granted
					when (grantResults[i]) {
						PackageManager.PERMISSION_GRANTED -> {
							// Update the UI accordingly
							setDayNightPermVisibility(false)
						}
						PackageManager.PERMISSION_DENIED -> {
							Toast.makeText(context, "Please grant the permission!", Toast.LENGTH_LONG).show()
							setDayNightPermVisibility(true)
						}
						else -> {
							Toast.makeText(context, "An unknown error occurred.", Toast.LENGTH_SHORT).show()
							Log.w(TAG, "Something went wrong when attempting to grant the permission.")
						}
					}
				}
			}
		}
	}

	/**
	 * Function to update the visibility of the day night preferences
	 * @param sharedPreferences An instance of [SharedPreferences]
	 */
	private fun updateDayNightPermVisibility(sharedPreferences: SharedPreferences?) {
		when (sharedPreferences?.getString(Constants.prefDarkTheme, "automatic")) {
			"automatic" -> {
				if (!isLocationPermGranted()) {
					setDayNightPermVisibility(true)
				}
			}
			else -> {
				// We don't want to show this when the user doesn't select "Auto" in the dark theme preference
				setDayNightPermVisibility(false)
			}
		}
	}

	/**
	 * Sets whether the day night permission info and grant preferences should be visible
	 * @param visibility [true] to show the preferences, [false] otherwise
	 */
	private fun setDayNightPermVisibility(visibility: Boolean) {
		prefDayNightGrantPerm?.isVisible = visibility
		prefDayNightPermInfo?.isVisible = visibility
	}

	/**
	 * Checks if the location permission is granted
	 * @return [true] if the location permission is granted, [false] otherwise
	 */
	private fun isLocationPermGranted() = ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

	companion object {
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}
