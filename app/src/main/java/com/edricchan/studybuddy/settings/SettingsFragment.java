package com.edricchan.studybuddy.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.edricchan.studybuddy.AccountActivity;
import com.edricchan.studybuddy.BuildConfig;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.utils.DataUtil;

public class SettingsFragment extends PreferenceFragmentCompat {

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_headers, rootKey);
		if (BuildConfig.DEBUG) {
			findPreference(DataUtil.prefHeaderDebug).setVisible(true);
		}
		findPreference(DataUtil.prefHeaderAccount)
				.setOnPreferenceClickListener(preference -> {
					startActivity(new Intent(getContext(), AccountActivity.class));
					return true;
				});
	}
}
