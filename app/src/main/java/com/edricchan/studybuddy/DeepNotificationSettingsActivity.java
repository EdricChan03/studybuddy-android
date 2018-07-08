package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by edricchan on 4/3/18.
 */

public class DeepNotificationSettingsActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent settingsIntent = new Intent(this, SettingsActivity.class);
		settingsIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.NotificationPreferenceFragment.class.getName());

		startActivity(settingsIntent);
		finish();
	}
}
