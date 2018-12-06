package com.edricchan.studybuddy.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.edricchan.studybuddy.BuildConfig;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.UpdatesActivity;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class AboutSettingsFragment extends PreferenceFragmentCompat {
	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.pref_versions, rootKey);
		final Context context = getActivity();
		final String appAuthorUrl = "https://github.com/Chan4077";
		final String appSrcUrl = "https://github.com/Chan4077/StudyBuddy";
		// String appIssueUrl = "https://github.com/Chan4077/StudyBuddy/issues/new";
		CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
		builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
		builder.addDefaultShareMenuItem();
		final CustomTabsIntent customTabsIntent = builder.build();
		Intent updatesIntent = new Intent(getActivity(), UpdatesActivity.class);
		findPreference("updates").setIntent(updatesIntent);
		Preference appAuthor = findPreference("app_author");
		assert appAuthor != null;
		appAuthor.setOnPreferenceClickListener(preference -> {
			customTabsIntent.launchUrl(context, Uri.parse(appAuthorUrl));
			return true;
		});
		Preference appSrc = findPreference("app_src_code");
		assert appSrc != null;
		appSrc.setOnPreferenceClickListener(preference -> {
			customTabsIntent.launchUrl(context, Uri.parse(appSrcUrl));
			return true;
		});
		Preference appVersion = findPreference("app_version");
		assert appVersion != null;
		appVersion.setSummary(BuildConfig.VERSION_NAME);
	}
}
