package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.edricchan.studybuddy.settings.AboutSettingsFragment;
import com.edricchan.studybuddy.settings.DebugSettingsFragment;
import com.edricchan.studybuddy.settings.GeneralSettingsFragment;
import com.edricchan.studybuddy.settings.SettingsFragment;
import com.edricchan.studybuddy.settings.SyncSettingsFragment;
import com.edricchan.studybuddy.settings.TodoSettingsFragment;
import com.edricchan.studybuddy.utils.DataUtil;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
	private static final String TAG = SharedHelper.getTag(SettingsActivity.class);
	private CustomTabsIntent tabsIntent;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
		builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
				.addDefaultShareMenuItem()
				.setShowTitle(true);
		tabsIntent = builder.build();
		if (savedInstanceState == null) {
			SharedHelper.replaceFragment(this, new SettingsFragment(), android.R.id.content, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_settings, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_send_feedback:
				tabsIntent.launchUrl(this, DataUtil.uriSendFeedback);
				return true;
			case R.id.action_help:
				Intent helpIntent = new Intent(this, HelpActivity.class);
				startActivity(helpIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
		// Instantiate the new Fragment
		final Bundle args = pref.getExtras();
		final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
				getClassLoader(),
				pref.getFragment(),
				args);
		if (fragment instanceof SettingsFragment) {
			setTitle("Settings");
		} else if (fragment instanceof AboutSettingsFragment) {
			setTitle("About");
		} else if (fragment instanceof DebugSettingsFragment) {
			setTitle("Debug");
		} else if (fragment instanceof GeneralSettingsFragment) {
			setTitle("General");
		} else if (fragment instanceof SyncSettingsFragment) {
			setTitle("Sync");
		} else if (fragment instanceof TodoSettingsFragment) {
			setTitle("Todos");
		}
		fragment.setArguments(args);
		fragment.setTargetFragment(caller, 0);
		// Replace the existing Fragment with the new Fragment
		SharedHelper.replaceFragment(this, fragment, android.R.id.content, true);
		return true;
	}
}
