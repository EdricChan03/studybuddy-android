package com.edricchan.studybuddy.settings;

import android.os.Bundle;
import android.widget.Toast;

import com.edricchan.studybuddy.R;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SyncSettingsFragment extends PreferenceFragmentCompat {
	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_data_sync, rootKey);
		Preference manualSyncPreference = findPreference("manual_sync");
		manualSyncPreference.setOnPreferenceClickListener(preference -> {
			Toast.makeText(getContext(), "Syncing...", Toast.LENGTH_SHORT).show();
			return true;
		});
		final SwitchPreferenceCompat cellularNetworksSync = (SwitchPreferenceCompat) findPreference("sync_cellular_networks");
		cellularNetworksSync.setOnPreferenceClickListener(preference -> {
			if (cellularNetworksSync.isChecked()) {
				cellularNetworksSync.setChecked(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setPositiveButton(R.string.dialog_action_yes, (dialog, which) -> cellularNetworksSync.setChecked(true));
				builder.setTitle(R.string.pref_sync_cellular_networks_dialog_title);
				builder.setMessage(R.string.pref_sync_cellular_networks_dialog_msg);
				builder.setNegativeButton(R.string.dialog_action_no, (dialog, which) -> dialog.dismiss());
				builder.show();
			}
			return true;
		});
	}
}
