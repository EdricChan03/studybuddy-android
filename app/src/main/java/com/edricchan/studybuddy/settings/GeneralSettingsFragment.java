package com.edricchan.studybuddy.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.utils.DataUtil;

public class GeneralSettingsFragment extends PreferenceFragmentCompat {
	private Preference prefDayNightPermInfo;
	private Preference prefDayNightGrantPerm;
	private PreferenceCategory prefCategoryTheme;
	private static final String TAG = SharedHelper.getTag(GeneralSettingsFragment.class);

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_general, rootKey);
		prefDayNightGrantPerm = findPreference(DataUtil.prefDaynightGrantPerm);
		prefDayNightPermInfo = findPreference(DataUtil.prefDayNightPermInfo);
		prefCategoryTheme = (PreferenceCategory) findPreference(DataUtil.prefCategoryTheme);
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			// Hide permission-related Preferences as the permission is already granted
			prefDayNightGrantPerm.setVisible(false);
			prefDayNightPermInfo.setVisible(false);
		} else {
			prefDayNightGrantPerm.setOnPreferenceClickListener(preference -> {
				requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
				return true;
			});
		}


	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 0) {
			for (int i = 0; i < permissions.length; i++) {
				if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
					// Check if the permission is granted
					switch (grantResults[i]) {
						case PackageManager.PERMISSION_GRANTED:
							// Update the UI accordingly
							prefDayNightGrantPerm.setVisible(false);
							prefDayNightPermInfo.setVisible(false);
							break;
						case PackageManager.PERMISSION_DENIED:
							Toast.makeText(getContext(), "Please grant the permission!", Toast.LENGTH_LONG).show();
							prefDayNightGrantPerm.setVisible(true);
							prefDayNightPermInfo.setVisible(true);
							break;
						default:
							Toast.makeText(getContext(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
							Log.w(TAG, "Something went wrong when attempting to grant the permission.");
							break;
					}
				}
			}
		}
	}
}
