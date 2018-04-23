package com.edricchan.studybuddy;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
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
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import java.util.ArrayList;
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
	private static final String sendFeedbackUrl = "https://goo.gl/forms/tz6cmNguIHuZMZIh2";
	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
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
					builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
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
		final List<MyNotificationChannel> notificationChannels = new ArrayList<>();
		final String[] notificationChannelIds = new String[]{"todo_updates", "weekly_summary", "sync", "app_updates", "playback", "uncategorised"};
		PreferenceScreen preferenceScreen;
		SharedPreferences sharedPreferences;

		/**
		 * Shows Oreo settings
		 */
		void showOreoSettings() {
			// Ensure that all preferences are cleared first
			preferenceScreen.removeAll();
			Preference allNotificationsPreference = new Preference(preferenceScreen.getContext());
			allNotificationsPreference.setTitle(R.string.pref_notification_channel_all_channels_title);
			allNotificationsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
						// Direct user to settings for notification channel
						Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
						intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
						startActivity(intent);
					}
					return true;
				}
			});
			preferenceScreen.addPreference(allNotificationsPreference);
			for (MyNotificationChannel notificationChannel : notificationChannels) {
				final PreferenceCategory notificationChannelCategory = new PreferenceCategory(preferenceScreen.getContext());
				notificationChannelCategory.setTitle(notificationChannel.getNotificationTitle());
				notificationChannelCategory.setSummary(notificationChannel.getNotificationDesc());
				notificationChannelCategory.setLayoutResource(R.layout.preference_category_summary);
				preferenceScreen.addPreference(notificationChannelCategory);
				final SwitchPreference notificationEnabledPreference = new SwitchPreference(preferenceScreen.getContext());
				notificationEnabledPreference.setTitle(R.string.pref_notification_channel_enabled_title);
				notificationEnabledPreference.setKey("notification_channel_" + notificationChannelIds[notificationChannel.getIndex()] + "_enabled");
				notificationEnabledPreference.setChecked(true);
				notificationChannelCategory.addPreference(notificationEnabledPreference);

				final Preference notificationAdvancedPreference = new Preference(preferenceScreen.getContext());
				notificationAdvancedPreference.setTitle(R.string.pref_notification_channel_advanced_title);
				notificationAdvancedPreference.setSummary(R.string.pref_notification_channel_advanced_desc);
				notificationAdvancedPreference.setKey(notificationChannelIds[notificationChannel.getIndex()]);
				notificationAdvancedPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
							Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
							intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
							intent.putExtra(Settings.EXTRA_CHANNEL_ID, preference.getKey());
							startActivity(intent);
						}
						return true;
					}
				});
				notificationChannelCategory.addPreference(notificationAdvancedPreference);
				if (notificationEnabledPreference.isChecked()) {
					notificationAdvancedPreference.setEnabled(true);
				} else {
					notificationAdvancedPreference.setEnabled(false);
				}
				notificationEnabledPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						if (notificationEnabledPreference.isChecked()) {
							notificationAdvancedPreference.setEnabled(true);
						} else {
							notificationAdvancedPreference.setEnabled(false);
						}
						return true;
					}
				});
			}
		}

		/**
		 * Shows settings for devices that are Android Nougat or lower
		 */
		void showPreOreoSettings() {
			// Ensure that all preferences are cleared first
			preferenceScreen.removeAll();
			// Toggle to enable all notification channels
			SwitchPreference enableAllNotificationChannelsPreference = new SwitchPreference(preferenceScreen.getContext());
			enableAllNotificationChannelsPreference.setTitle(R.string.pref_enable_all_notification_channels_title);
			enableAllNotificationChannelsPreference.setKey("enable_all_notification_channels");
			preferenceScreen.addPreference(enableAllNotificationChannelsPreference);
			SwitchPreference enableMoreOptionsPreference = new SwitchPreference(preferenceScreen.getContext());
			enableMoreOptionsPreference.setTitle(R.string.pref_enable_more_options_title);
			enableMoreOptionsPreference.setSummary(R.string.pref_enable_more_options_desc);
			enableMoreOptionsPreference.setKey("enable_more_options_notification_channels");
			enableMoreOptionsPreference.setIcon(R.drawable.ic_plus_box_24dp);
			preferenceScreen.addPreference(enableMoreOptionsPreference);
			enableMoreOptionsPreference.setDependency("enable_all_notification_channels");
			for (MyNotificationChannel notificationChannel : notificationChannels) {
				PreferenceCategory preOreoNotificationChannelCategory = new PreferenceCategory(preferenceScreen.getContext());
				preferenceScreen.addPreference(preOreoNotificationChannelCategory);
				preOreoNotificationChannelCategory.setTitle(notificationChannel.getNotificationTitle());
				preOreoNotificationChannelCategory.setDependency("enable_all_notification_channels");
				// Description of notification channel
				Preference preOreoNotificationDescPreference = new Preference(preferenceScreen.getContext());
				preOreoNotificationDescPreference.setEnabled(false);
				preOreoNotificationDescPreference.setTitle("About this channel");
				preOreoNotificationDescPreference.setSummary(notificationChannel.getNotificationDesc());
				preOreoNotificationChannelCategory.addPreference(preOreoNotificationDescPreference);
				// Enable notification preference
				SwitchPreference enableNotificationPreference = new SwitchPreference(preferenceScreen.getContext());
				enableNotificationPreference.setChecked(true);
				enableNotificationPreference.setTitle("Enable notification channel");
				enableNotificationPreference.setKey("notification_channel_" + getString(notificationChannel.getNotificationId()) + "_enabled");
				preOreoNotificationChannelCategory.addPreference(enableNotificationPreference);
				// Enable vibration preference
				SwitchPreference enableVibratePreference = new SwitchPreference(preferenceScreen.getContext());
				enableVibratePreference.setTitle("Vibrate");
				enableVibratePreference.setSummary("Whether to vibrate when you receive a notification from this channel");
				enableVibratePreference.setChecked(true);
				enableVibratePreference.setIcon(R.drawable.ic_vibrate_24dp);
				enableVibratePreference.setKey("notification_channel_" + getString(notificationChannel.getNotificationId()) + "_vibrate_enabled");
				preOreoNotificationChannelCategory.addPreference(enableVibratePreference);
				RingtonePreference notificationRingtonePreference = new RingtonePreference(preferenceScreen.getContext());
				notificationRingtonePreference.setIcon(R.drawable.ic_music_24dp);
				notificationRingtonePreference.setTitle("Set ringtone");
				notificationRingtonePreference.setKey("notification_channel_" + getString(notificationChannel.getNotificationId()) + "_ringtone");
				bindPreferenceSummaryToValue(notificationRingtonePreference);
				preOreoNotificationChannelCategory.addPreference(notificationRingtonePreference);
				enableVibratePreference.setDependency("notification_channel_" + getString(notificationChannel.getNotificationId()) + "_enabled");
				notificationRingtonePreference.setDependency("notification_channel_" + getString(notificationChannel.getNotificationId()) + "_enabled");
			}
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_notification);
			setHasOptionsMenu(true);
			preferenceScreen = this.getPreferenceScreen();
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
			// Add notification channels to list
			// Check if list is empty
			if (notificationChannels.isEmpty()) {
				notificationChannels.add(new MyNotificationChannel(R.string.notification_channel_todo_updates_title, R.string.notification_channel_todo_updates_desc, R.string.notification_channel_todo_updates_id, 0));
				notificationChannels.add(new MyNotificationChannel(R.string.notification_channel_weekly_summary_title, R.string.notification_channel_weekly_summary_desc, R.string.notification_channel_weekly_summary_id, 1));
				notificationChannels.add(new MyNotificationChannel(R.string.notification_channel_sync_title, R.string.notification_channel_sync_desc, R.string.notification_channel_sync_id, 2));
				notificationChannels.add(new MyNotificationChannel(R.string.notification_channel_app_updates_title, R.string.notification_channel_app_updates_desc, R.string.notification_channel_app_updates_id, 3));
				notificationChannels.add(new MyNotificationChannel(R.string.notification_channel_playback_title, R.string.notification_channel_playback_desc, R.string.notification_channel_playback_id, 4));
				notificationChannels.add(new MyNotificationChannel(R.string.notification_channel_uncategorised_title, R.string.notification_channel_uncategorised_desc, R.string.notification_channel_uncategorised_id, 5));
			}
			/*
			Check if user is running on Android Oreo since there's notification channels support
			- Removes the multi select preference
			*/
			if (sharedPreferences.getBoolean("enable_pre_oreo_explicit", false) || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
				// User is using pre-Oreo or has enabled the explicit pre-Oreo checkbox
				System.out.println("Showing pre-Oreo settings");
				showPreOreoSettings();
			} else {
				System.out.println("Showing Oreo settings");
				// User is using Android Oreo
				showOreoSettings();
			}
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
					builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
					builder.addDefaultShareMenuItem();
					final CustomTabsIntent customTabsIntent = builder.build();
					customTabsIntent.launchUrl(getContext(), Uri.parse(sendFeedbackUrl));
					return true;
				case R.id.action_help:
					// TODO: Add some stuff
					return true;
				case R.id.action_checkbox_ui_pre_oreo:
					if (item.isChecked()) {
						item.setChecked(false);
						System.out.println("Checked -> Unchecked");
						sharedPreferences.edit()
								.putBoolean("enable_pre_oreo_explicit", false)
								.apply();
					} else {
						item.setChecked(true);
						System.out.println("Unchecked -> checked");
						sharedPreferences.edit()
								.putBoolean("enable_pre_oreo_explicit", true)
								.apply();
					}
					preferenceScreen.removeAll();
//					addPreferencesFromResource(R.xml.pref_notification);
					onCreate(null);
//					if (sharedPreferences.getBoolean("enable_pre_oreo_explicit", false) || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//						showPreOreoSettings();
//					} else {
//						showOreoSettings();
//					}
					return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.menu_settings_notifications, menu);
			if (BuildConfig.DEBUG) {
				menu.findItem(R.id.action_checkbox_ui_pre_oreo)
						.setVisible(true)
						.setChecked(sharedPreferences.getBoolean("enable_pre_oreo_explicit", false));
			}
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
			Preference manualSyncPreference = findPreference("manual_sync");
			manualSyncPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					Crashlytics.getInstance().crash();
					return true;
				}
			});
			final SwitchPreference mobileDataSync = (SwitchPreference) findPreference("sync_mobile_data");
			mobileDataSync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (mobileDataSync.isChecked()) {
						mobileDataSync.setChecked(false);
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setPositiveButton(R.string.dialog_action_yes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mobileDataSync.setChecked(true);
							}
						});
						builder.setTitle(R.string.pref_sync_mobile_data_dialog_title);
						builder.setMessage(R.string.pref_sync_mobile_data_dialog_msg);
						builder.setNegativeButton(R.string.dialog_action_no, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						builder.create().show();
					}
					return true;
				}
			});
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
					builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
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
			// String appIssueUrl = "https://github.com/Chan4077/StudyBuddy/issues/new";
			CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
			builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
			builder.addDefaultShareMenuItem();
			final CustomTabsIntent customTabsIntent = builder.build();
			final Preference checkForUpdates = getPreferenceManager().findPreference("check_for_updates");
			checkForUpdates.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					if (preference.getKey().equals("check_for_updates")) {
						final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
						final NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(getContext(), getString(R.string.notification_channel_app_updates_id))
								.setSmallIcon(R.drawable.ic_notification_system_update_24dp)
								.setContentTitle(getString(R.string.notification_check_update))
								.setPriority(NotificationCompat.PRIORITY_DEFAULT)
								.setProgress(100, 0, true)
								.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
								.setOngoing(true);
						notificationManager.notify(0, notifyBuilder.build());
						checkForUpdates.setEnabled(false);
						new android.os.Handler().postDelayed(
								new Runnable() {
									public void run() {
										notifyBuilder.setContentTitle(getString(R.string.notification_no_updates))
												.setProgress(0, 0, false)
												.setOngoing(false);
										notificationManager.notify(0, notifyBuilder.build());
										new android.os.Handler().postDelayed(
												new Runnable() {
													@Override
													public void run() {
														checkForUpdates.setEnabled(true);
														notificationManager.cancel(0);
													}
												},
												2000);
									}
								},
								10000);
						AppUpdater appUpdater = new AppUpdater(getActivity())
								.setUpdateFrom(UpdateFrom.JSON)
								.setUpdateJSON("https://raw.githubusercontent.com/Chan4077/StudyBuddy-builds/master/release/changelog.json")
								.setDisplay(Display.NOTIFICATION)
								.setIcon(R.drawable.ic_alert_decagram_24dp);
						appUpdater.start();
					}
					return true;
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
					return true;
				}
			});
			Preference appSrc = getPreferenceManager().findPreference("app_src_code");
			appSrc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					customTabsIntent.launchUrl(context, Uri.parse(appSrcUrl));
					return true;
				}
			});
			Preference appIntro = getPreferenceManager().findPreference("app_intro");
			appIntro.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					Intent appIntroIntent = new Intent(context, MyIntroActivity.class);
					startActivity(appIntroIntent);
					return true;
				}
			});
			Preference appVersion = getPreferenceManager().findPreference("app_version");
			appVersion.setSummary(getVersion());
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
					builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
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
		@SuppressWarnings("SameReturnValue")
		public String getVersion() {
			return BuildConfig.VERSION_NAME;
		}

		/**
		 * Gets the version code of the app
		 *
		 * @return The version code of the app (integer)
		 */
		@SuppressWarnings("SameReturnValue")
		public int getVersionCode() {
			return BuildConfig.VERSION_CODE;
		}
	}
}
