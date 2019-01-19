package com.edricchan.studybuddy.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat

import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.utils.DataUtils

class GeneralSettingsFragment : PreferenceFragmentCompat() {
	private var prefDayNightPermInfo: Preference? = null
	private var prefDayNightGrantPerm: Preference? = null
	private var prefCategoryTheme: PreferenceCategory? = null

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_general, rootKey)
		prefDayNightGrantPerm = findPreference(DataUtils.prefDaynightGrantPerm)
		prefDayNightPermInfo = findPreference(DataUtils.prefDayNightPermInfo)
		prefCategoryTheme = findPreference<Preference>(DataUtils.prefCategoryTheme) as PreferenceCategory
		if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			// Hide permission-related Preferences as the permission is already granted
			prefDayNightGrantPerm!!.isVisible = false
			prefDayNightPermInfo!!.isVisible = false
		} else {
			prefDayNightGrantPerm!!.setOnPreferenceClickListener {
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
							prefDayNightGrantPerm!!.isVisible = false
							prefDayNightPermInfo!!.isVisible = false
						}
						PackageManager.PERMISSION_DENIED -> {
							Toast.makeText(context, "Please grant the permission!", Toast.LENGTH_LONG).show()
							prefDayNightGrantPerm!!.isVisible = true
							prefDayNightPermInfo!!.isVisible = true
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

	companion object {
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}
