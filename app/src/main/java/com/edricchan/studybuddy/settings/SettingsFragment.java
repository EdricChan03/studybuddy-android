package com.edricchan.studybuddy.settings;

import android.os.Bundle;

import com.edricchan.studybuddy.BuildConfig;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.utils.DataUtil;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_headers, rootKey);
		if (BuildConfig.DEBUG) {
			findPreference(DataUtil.prefHeaderDebug).setVisible(true);
		}
	}
}
