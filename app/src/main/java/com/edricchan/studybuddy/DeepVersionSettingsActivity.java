package com.edricchan.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.appcompat.app.AppCompatActivity;

public class DeepVersionSettingsActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent settingsIntent = new Intent(this, SettingsActivity.class);
		settingsIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.VersionPreferenceFragment.class.getName());

		startActivity(settingsIntent);
		finish();
	}
}
