package com.edricchan.studybuddy;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.edricchan.studybuddy.settings.DebugSettingsFragment;
import com.edricchan.studybuddy.utils.SharedUtils;

/**
 * Created by edricchan on 14/3/18.
 */

public class DebugActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		SharedUtils.Companion.replaceFragment(this, new DebugSettingsFragment(), android.R.id.content, false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
