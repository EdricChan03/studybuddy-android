package com.edricchan.studybuddy;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
	static String sendFeedbackUrl = "https://goo.gl/forms/tz6cmNguIHuZMZIh2";
	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference.setSummary(
						index >= 0
								? listPreference.getEntries()[index]
								: null);

			} else if (preference instanceof RingtonePreference) {
				// For ringtone preferences, look up the correct display value
				// using RingtoneManager.
				if (TextUtils.isEmpty(stringValue)) {
					// Empty values correspond to 'silent' (no ringtone).
					preference.setSummary(R.string.pref_ringtone_silent);

				} else {
					Ringtone ringtone = RingtoneManager.getRingtone(
							preference.getContext(), Uri.parse(stringValue));

					if (ringtone == null) {
						// Clear the summary if there was a lookup error.
						preference.setSummary(null);
					} else {
						// Set the summary to reflect the new ringtone display
						// name.
						String name = ringtone.getTitle(preference.getContext());
						preference.setSummary(name);
					}
				}

			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 *
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
				PreferenceManager
						.getDefaultSharedPreferences(preference.getContext())
						.getString(preference.getKey(), ""));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			if (!super.onMenuItemSelected(featureId, item)) {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.pref_headers, target);
	}

	/**
	 * This method stops fragment injection in malicious applications.
	 * Make sure to deny any unknown fragments here.
	 */
	protected boolean isValidFragment(String fragmentName) {
		return PreferenceFragment.class.getName().equals(fragmentName)
				|| ExperimentalPreferenceFragment.class.getName().equals(fragmentName)
				|| DataSyncPreferenceFragment.class.getName().equals(fragmentName)
				|| NotificationPreferenceFragment.class.getName().equals(fragmentName)
				|| VersionPreferenceFragment.class.getName().equals(fragmentName);
	}

	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ExperimentalPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_experimental);
			setHasOptionsMenu(true);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
//            bindPreferenceSummaryToValue(findPreference("example_text"));
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			switch (id) {
				case android.R.id.home:
					startActivity(new Intent(getActivity(), SettingsActivity.class));
					return true;
				case R.id.action_send_feedback:
					CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
					builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
					builder.addDefaultShareMenuItem();
					final CustomTabsIntent customTabsIntent = builder.build();
					customTabsIntent.launchUrl(getContext(), Uri.parse(sendFeedbackUrl));
					return true;
				case R.id.action_help:
					// TODO: Add some stuff
					return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.menu_settings, menu);
		}
	}

	/**
	 * This fragment shows notification preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NotificationPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_notification);
			setHasOptionsMenu(true);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			switch (id) {
				case android.R.id.home:
					startActivity(new Intent(getActivity(), SettingsActivity.class));
					return true;
				case R.id.action_send_feedback:
					CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
					builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
					builder.addDefaultShareMenuItem();
					final CustomTabsIntent customTabsIntent = builder.build();
					customTabsIntent.launchUrl(getContext(), Uri.parse(sendFeedbackUrl));
					return true;
				case R.id.action_help:
					// TODO: Add some stuff
					return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.menu_settings, menu);
		}

		public void registerOnSharedPreferenceChangeListener() {
			SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
				@Override
				public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
					boolean notifIsOn = sharedPreferences.getBoolean(getString(R.string.pref_title_new_message_notifications), true);
					// TODO
//                    SwitchPreference notifSwitchPreference = (SwitchPreference) getFragmentManager().
					if (notifIsOn) {
					} else {

					}
				}
			};
		}
	}

	/**
	 * This fragment shows data and sync preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DataSyncPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_data_sync);
			setHasOptionsMenu(true);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("sync_frequency"));
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			switch (id) {
				case android.R.id.home:
					startActivity(new Intent(getActivity(), SettingsActivity.class));
					return true;
				case R.id.action_send_feedback:
					CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
					builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
					builder.addDefaultShareMenuItem();
					final CustomTabsIntent customTabsIntent = builder.build();
					customTabsIntent.launchUrl(getContext(), Uri.parse(sendFeedbackUrl));
					return true;
				case R.id.action_help:
					// TODO: Add some stuff
					return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.menu_settings, menu);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class VersionPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_versions);
			setHasOptionsMenu(true);
			final Context context = getContext();
			final String appAuthorUrl = "https://github.com/Chan4077";
			final String appSrcUrl = "https://github.com/Chan4077/StudyBuddy";
			String appIssueUrl = "https://github.com/Chan4077/StudyBuddy/issues/new";
			CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
			builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
			builder.addDefaultShareMenuItem();
			final CustomTabsIntent customTabsIntent = builder.build();
			Preference checkForUpdates = getPreferenceManager().findPreference("check_for_updates");
			checkForUpdates.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (preference.getKey().equals("check_for_updates")) {
						Snackbar.make(getView(), getString(R.string.snackbar_check_updates), 4000).show();
						AppUpdater appUpdater = new AppUpdater(getActivity())
								.setUpdateFrom(UpdateFrom.JSON)
								.setUpdateJSON("https://raw.githubusercontent.com/Chan4077/StudyBuddy-builds/master/release/changelog.json")
								.setIcon(R.drawable.ic_alert_decagram_white_24dp);
						appUpdater.start();
						return true;
					}
					return false;
				}
			});
			Preference appAuthor = getPreferenceManager().findPreference("app_author");
			appAuthor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (preference.getKey().equals("app_author")) {
						customTabsIntent.launchUrl(context, Uri.parse(appAuthorUrl));
						return true;
					}
					return false;
				}
			});
			Preference appSrc = getPreferenceManager().findPreference("app_src_code");
			appSrc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (preference.getKey().equals("app_src_code")) {
						customTabsIntent.launchUrl(context, Uri.parse(appSrcUrl));
						return true;
					}
					return false;
				}
			});
			Preference appIntro = getPreferenceManager().findPreference("app_intro");
			appIntro.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (preference.getKey().equals("app_intro")) {
						Intent appIntroIntent = new Intent(context, MyIntroActivity.class);
						startActivity(appIntroIntent);
						return true;
					}
					return false;
				}
			});
			Preference appVer = getPreferenceManager().findPreference("app_ver");
			appVer.setSummary(getVersion());
			bindPreferenceSummaryToValue(findPreference("updates_channel"));
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			switch (id) {
				case android.R.id.home:
					startActivity(new Intent(getActivity(), SettingsActivity.class));
					return true;
				case R.id.action_send_feedback:
					CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
					builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
					builder.addDefaultShareMenuItem();
					final CustomTabsIntent customTabsIntent = builder.build();
					customTabsIntent.launchUrl(getContext(), Uri.parse(sendFeedbackUrl));
					return true;
				case R.id.action_help:
					// TODO: Add some stuff
					return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.menu_settings, menu);
		}

		/**
		 * Gets the version of the app
		 *
		 * @return The version of the app (string)
		 */
		public String getVersion() {
			return BuildConfig.VERSION_NAME;
		}

		/**
		 * Gets the version code of the app
		 *
		 * @return The version code of the app (integer)
		 */
		public int getVersionCode() {
			return BuildConfig.VERSION_CODE;
		}
	}
}
