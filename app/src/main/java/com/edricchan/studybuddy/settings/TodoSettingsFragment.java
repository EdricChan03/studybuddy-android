package com.edricchan.studybuddy.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.PreferenceFragmentCompat;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.utils.DataUtil;

public class TodoSettingsFragment extends PreferenceFragmentCompat {
	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_todos, rootKey);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
			intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
			intent.putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notification_channel_weekly_summary_id));
			findPreference(DataUtil.prefWeeklySummaryNotificationOpts)
					.setIntent(intent);
		} else {
			// Hide the preference as notification channels aren't implemented on lower versions of Android
			findPreference(DataUtil.prefWeeklySummaryNotificationOpts)
					.setVisible(false);
		}
	}
}
