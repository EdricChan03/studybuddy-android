package com.edricchan.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by edricchan on 4/3/18.
 */

public class DeepSettingsFragmentDummyActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent settingsIntent = new Intent(this, SettingsActivity.class);
		settingsIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.NotificationPreferenceFragment.class.getName());

		startActivity(settingsIntent);
		finish();
	}
}
