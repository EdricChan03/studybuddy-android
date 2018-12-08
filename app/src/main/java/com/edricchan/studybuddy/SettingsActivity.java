package com.edricchan.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.edricchan.studybuddy.settings.AboutSettingsFragment;
import com.edricchan.studybuddy.settings.DebugSettingsFragment;
import com.edricchan.studybuddy.settings.GeneralSettingsFragment;
import com.edricchan.studybuddy.settings.SettingsFragment;
import com.edricchan.studybuddy.settings.SyncSettingsFragment;
import com.edricchan.studybuddy.settings.TodoSettingsFragment;
import com.edricchan.studybuddy.utils.DataUtil;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
	private SharedPreferences preferences;
	private static final String TAG = SharedHelper.getTag(SettingsActivity.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
				SharedHelper.launchUri(this, DataUtil.uriSendFeedback, preferences.getBoolean(DataUtil.prefUseCustomTabs, true));
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
