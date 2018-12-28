package com.edricchan.studybuddy.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.edricchan.studybuddy.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SyncSettingsFragment : PreferenceFragmentCompat() {
	override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
		setPreferencesFromResource(R.xml.pref_data_sync, rootKey)
		val manualSyncPreference = findPreference<Preference>("manual_sync")
		manualSyncPreference.setOnPreferenceClickListener {
			Toast.makeText(context, "Syncing...", Toast.LENGTH_SHORT).show()
			true
		}
		val cellularNetworksSync = findPreference<Preference>("sync_cellular_networks") as SwitchPreferenceCompat
		cellularNetworksSync.setOnPreferenceClickListener {
			if (cellularNetworksSync.isChecked) {
				cellularNetworksSync.isChecked = false
				val builder = MaterialAlertDialogBuilder(activity!!)
				builder.setPositiveButton(R.string.dialog_action_yes) { _, _ -> cellularNetworksSync.isChecked = true }
				builder.setTitle(R.string.pref_sync_cellular_networks_dialog_title)
				builder.setMessage(R.string.pref_sync_cellular_networks_dialog_msg)
				builder.setNegativeButton(R.string.dialog_action_no) { dialog, _ -> dialog.dismiss() }
				builder.show()
			}
			true
		}
	}
}
