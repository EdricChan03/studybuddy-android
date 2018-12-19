package com.edricchan.studybuddy.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.edricchan.studybuddy.AccountActivity;
import com.edricchan.studybuddy.BuildConfig;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.utils.DataUtil;

public class SettingsFragment extends PreferenceFragmentCompat {

	private SharedPreferences preferences;

	@Override
	public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_headers, rootKey);
		preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		if (BuildConfig.DEBUG) {
			findPreference(DataUtil.prefHeaderDebug).setVisible(true);
		}
		SwitchPreferenceCompat showHeaderSummaryPref = (SwitchPreferenceCompat) findPreference(DataUtil.prefShowHeaderSummary);
		showHeaderSummaryPref.setOnPreferenceClickListener(preference -> {
			updateHeaderSummaries(preferences.getBoolean(DataUtil.prefShowHeaderSummary, false));
			return true;
		});
		findPreference(DataUtil.prefHeaderAccount)
				.setOnPreferenceClickListener(preference -> {
					startActivity(new Intent(getContext(), AccountActivity.class));
					return true;
				});
		updateHeaderSummaries(preferences.getBoolean(DataUtil.prefShowHeaderSummary, false));
	}

	private void updateHeaderSummaries(boolean showHeaderSummaries) {
		if (showHeaderSummaries) {
			findPreference(DataUtil.prefHeaderAbout).setSummary(R.string.pref_header_version_summary);
			findPreference(DataUtil.prefHeaderAccount).setSummary(R.string.pref_header_account_summary);
			findPreference(DataUtil.prefHeaderDebug).setSummary(R.string.pref_header_debug_summary);
			findPreference(DataUtil.prefHeaderGeneral).setSummary(R.string.pref_header_general_summary);
			findPreference(DataUtil.prefHeaderSync).setSummary(R.string.pref_header_data_sync_summary);
			findPreference(DataUtil.prefHeaderTodo).setSummary(R.string.pref_header_todo_summary);
		} else {
			findPreference(DataUtil.prefHeaderAbout).setSummary(null);
			findPreference(DataUtil.prefHeaderAccount).setSummary(null);
			findPreference(DataUtil.prefHeaderDebug).setSummary(null);
			findPreference(DataUtil.prefHeaderGeneral).setSummary(null);
			findPreference(DataUtil.prefHeaderSync).setSummary(null);
			findPreference(DataUtil.prefHeaderTodo).setSummary(null);
		}
	}
}
