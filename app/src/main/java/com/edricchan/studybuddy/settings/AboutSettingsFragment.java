package com.edricchan.studybuddy.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.edricchan.studybuddy.BuildConfig;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.UpdatesActivity;
import com.edricchan.studybuddy.utils.DataUtil;

public class AboutSettingsFragment extends PreferenceFragmentCompat {
	private SharedPreferences preferences;

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_versions, rootKey);
		final Context context = getActivity();
		preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		final Uri appAuthorUrl = Uri.parse("https://github.com/Chan4077");
		findPreference(DataUtil.prefUpdates).setIntent(new Intent(getActivity(), UpdatesActivity.class));
		Preference appAuthor = findPreference(DataUtil.prefAppAuthor);
		appAuthor.setOnPreferenceClickListener(preference -> {
			SharedHelper.launchUri(context, appAuthorUrl, preferences.getBoolean(DataUtil.prefUseCustomTabs, true));
			return true;
		});
		Preference appSrc = findPreference(DataUtil.prefAppSrcCode);
		appSrc.setOnPreferenceClickListener(preference -> {
			SharedHelper.launchUri(context, DataUtil.uriSrcCode, preferences.getBoolean(DataUtil.prefUseCustomTabs, true));
			return true;
		});
		Preference appVersion = findPreference(DataUtil.prefAppVersion);
		appVersion.setSummary(BuildConfig.VERSION_NAME);

		Preference appInfo = findPreference(DataUtil.prefAppInfo);
		appInfo.setIntent(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
	}
}
