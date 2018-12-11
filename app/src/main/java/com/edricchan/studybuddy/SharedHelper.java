package com.edricchan.studybuddy;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.edricchan.studybuddy.interfaces.NotificationRequest;
import com.edricchan.studybuddy.interfaces.TaskItem;
import com.edricchan.studybuddy.receiver.ActionButtonReceiver;
import com.edricchan.studybuddy.utils.DataUtil;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedHelper {
	/**
	 * Intent for notification settings action button for notifications
	 *
	 * @deprecated Use {@link DataUtil#actionNotificationsSettingsIntent}
	 */
	@Deprecated
	public static final String ACTION_NOTIFICATIONS_SETTINGS_INTENT = DataUtil.actionNotificationsSettingsIntent;
	/**
	 * Broadcaster for starting download
	 *
	 * @deprecated Use {@link DataUtil#actionNotificationsStartDownloadReceiver}
	 */
	@Deprecated
	public static final String ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER = DataUtil.actionNotificationsStartDownloadReceiver;
	/**
	 * Broadcaster for retrying check for updates
	 *
	 * @deprecated Use {@link DataUtil#actionNotificationsRetryCheckForUpdateReceiver}
	 */
	@Deprecated
	public static final String ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER = DataUtil.actionNotificationsRetryCheckForUpdateReceiver;
	/**
	 * Action icon for settings
	 *
	 * @deprecated Use {@link DataUtil#fcmSettingsIcon}
	 */
	@Deprecated
	public static final String ACTION_SETTINGS_ICON = DataUtil.fcmSettingsIcon;
	/**
	 * Action icon for notification
	 *
	 * @deprecated Use {@link DataUtil#fcmNotificationIcon}
	 */
	@Deprecated
	public static final String ACTION_NOTIFICATION_ICON = DataUtil.fcmNotificationIcon;
	/**
	 * Action icon for mark as done
	 *
	 * @deprecated Use {@link DataUtil#fcmMarkAsDoneIcon}
	 */
	@Deprecated
	public static final String ACTION_MARK_AS_DONE_ICON = DataUtil.fcmMarkAsDoneIcon;
	// IDs for notifications
	/**
	 * ID for checking for updates
	 *
	 * @deprecated Use {@link DataUtil#notificationCheckForUpdatesId}
	 */
	@Deprecated
	public static final int NOTIFICATION_CHECK_FOR_UPDATES = DataUtil.notificationCheckForUpdatesId;
	/**
	 * ID for mMediaPlayer notification
	 *
	 * @deprecated Use {@link DataUtil#notificationMediaId}
	 */
	@Deprecated
	public static final int NOTIFICATION_MEDIA = DataUtil.notificationMediaId;
	/**
	 * {@link android.content.SharedPreferences} file used for all classes
	 *
	 * @deprecated Use {@link DataUtil#defaultSharedPrefsFile}
	 */
	@Deprecated
	public static final String DEFAULT_SHARED_PREFS_FILE = DataUtil.defaultSharedPrefsFile;

	// Context
	private Context mContext;
	// Since IDs 0 and 1 have been taken, use 2
	private int dynamicId = 2;
	private AtomicInteger atomicInteger = new AtomicInteger(dynamicId);

	/**
	 * @param context The context used for all methods
	 */
	public SharedHelper(Context context) {
		this.mContext = context;
	}

	/**
	 * @deprecated
	 */
	public SharedHelper() {
	}

	/**
	 * Utility method to set the app's theme.
	 * Note that the activity should be recreated after this function is called in order for the theme to be applied.
	 *
	 * @param context The context
	 */
	public static void setAppTheme(Context context) {
		String appTheme = PreferenceManager.getDefaultSharedPreferences(context).getString(DataUtil.prefDarkTheme, "1");
		assert appTheme != null;
		switch (appTheme) {
			case "1":
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
				break;
			case "2":
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
				break;
			case "3":
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
				break;
			default:
				Log.w(getTag(SharedHelper.class), "Please supply a valid string integer (1, 2, or 3)!");
				break;
		}
	}

	/**
	 * Replaces a view with an initialised fragment.
	 * Note: This method checks if there's already a fragment in the view.
	 *
	 * @param activity       The activity.
	 * @param fragment       The fragment to replace the view with. (Needs to be initialised with a `new` constructor).
	 * @param viewId         The ID of the view.
	 * @param tag            The tag to assign to the fragment.
	 * @param addToBackStack Whether to add the fragment to the back stack.
	 * @return True if the fragment was replaced, false if there's already an existing fragment.
	 */
	public static boolean replaceFragment(AppCompatActivity activity, Fragment fragment, @IdRes int viewId, String tag, boolean addToBackStack) {
		// Check if fragment already has been replaced
		if ((activity.getSupportFragmentManager().findFragmentByTag(tag) != fragment) &&
				(activity.getSupportFragmentManager().findFragmentById(viewId) != fragment)) {
			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
			transaction.replace(
					viewId,
					fragment,
					tag
			);
			if (addToBackStack) {
				transaction.addToBackStack(null);
			}
			transaction.commit();
			// Indicate that the fragment replacement has been done.
			return true;
		}
		// Return false if there's already an existing fragment.
		return false;
	}

	/**
	 * Replaces a view with an initialised fragment.
	 * Note: This method checks if there's already a fragment in the view.
	 *
	 * @param activity       The activity.
	 * @param fragment       The fragment to replace the view with. (Needs to be initialised with a `new` constructor).
	 * @param viewId         The ID of the view.
	 * @param addToBackStack Whether to add the fragment to the back stack.
	 * @return True if the fragment was replaced, false if there's already an existing fragment.
	 */
	public static boolean replaceFragment(AppCompatActivity activity, Fragment fragment, @IdRes int viewId, boolean addToBackStack) {
		if (activity.getSupportFragmentManager().findFragmentById(viewId) != fragment) {
			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
			transaction.replace(
					viewId,
					fragment
			);
			if (addToBackStack) {
				transaction.addToBackStack(null);
			}
			transaction.commit();
			// Indicate that the fragment replacement has been done.
			return true;
		}
		// Return false if there's already an existing fragment.
		return false;
	}

	/**
	 * Saves preferences to {@link SharedPreferences}
	 *
	 * @param context   The context
	 * @param prefsFile The preference file to save to
	 * @param mode      The mode of writing the file.
	 * @param key       The preference key to save to
	 * @param value     The value to save (as a boolean)
	 * @return An instance of the {@link SharedPreferences.Editor}
	 */
	public static SharedPreferences.Editor putPrefs(Context context, String prefsFile, int mode, String key, boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(prefsFile, mode);
		return preferences.edit()
				.putBoolean(key, value);
	}

	/**
	 * Saves preferences to {@link SharedPreferences}
	 *
	 * @param context   The context
	 * @param prefsFile The preference file to save to
	 * @param mode      The mode of writing the file.
	 * @param key       The preference key to save to
	 * @param value     The value to save (as a float)
	 * @return An instance of the {@link SharedPreferences.Editor}
	 */
	public static SharedPreferences.Editor putPrefs(Context context, String prefsFile, int mode, String key, float value) {
		SharedPreferences preferences = context.getSharedPreferences(prefsFile, mode);
		return preferences.edit()
				.putFloat(key, value);
	}

	/**
	 * Saves preferences to {@link SharedPreferences}
	 *
	 * @param context   The context
	 * @param prefsFile The preference file to save to
	 * @param mode      The mode of writing the file.
	 * @param key       The preference key to save to
	 * @param value     The value to save (as an integer)
	 * @return An instance of the {@link SharedPreferences.Editor}
	 */
	public static SharedPreferences.Editor putPrefs(Context context, String prefsFile, int mode, String key, int value) {
		SharedPreferences preferences = context.getSharedPreferences(prefsFile, mode);
		return preferences.edit()
				.putInt(key, value);
	}

	/**
	 * Saves preferences to {@link SharedPreferences}
	 *
	 * @param context   The context
	 * @param prefsFile The preference file to save to
	 * @param mode      The mode of writing the file.
	 * @param key       The preference key to save to
	 * @param value     The value to save (as a long)
	 * @return An instance of the {@link SharedPreferences.Editor}
	 */
	public static SharedPreferences.Editor putPrefs(Context context, String prefsFile, int mode, String key, long value) {
		SharedPreferences preferences = context.getSharedPreferences(prefsFile, mode);
		return preferences.edit()
				.putLong(key, value);
	}

	/**
	 * Saves preferences to {@link SharedPreferences}
	 *
	 * @param context   The context
	 * @param prefsFile The preference file to save to
	 * @param mode      The mode of writing the file.
	 * @param key       The preference key to save to
	 * @param value     The value to save (as a string)
	 * @return An instance of the {@link SharedPreferences.Editor}
	 */
	public static SharedPreferences.Editor putPrefs(Context context, String prefsFile, int mode, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences(prefsFile, mode);
		return preferences.edit()
				.putString(key, value);
	}

	/**
	 * Saves preferences to {@link SharedPreferences}
	 *
	 * @param context   The context
	 * @param prefsFile The preference file to save to
	 * @param mode      The mode of writing the file.
	 * @param key       The preference key to save to
	 * @param value     The value to save (as a {@link Set<String>})
	 * @return An instance of the {@link SharedPreferences.Editor}
	 */
	public static SharedPreferences.Editor putPrefs(Context context, String prefsFile, int mode, String key, Set<String> value) {
		SharedPreferences preferences = context.getSharedPreferences(prefsFile, mode);
		return preferences.edit()
				.putStringSet(key, value);
	}

	/**
	 * @param datePicker The datepicker
	 * @return a java.util.Date
	 */
	public static Date getDateFromDatePicker(DatePicker datePicker) {
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year = datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime();
	}

	/**
	 * Checks whether the network is available
	 *
	 * @param context The context
	 * @return A boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		assert connectivityManager != null;
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Checks whether the network is unavailable
	 *
	 * @param context The context
	 * @return A boolean
	 * @deprecated Use {@link SharedHelper#isNetworkAvailable(Context)}
	 */
	public static boolean isNetworkUnavailable(Context context) {
		return !isNetworkAvailable(context);
	}

	/**
	 * Checks whether the network is cellular
	 *
	 * @param context The context
	 * @return A boolean
	 * See https://stackoverflow.com/a/32771164
	 */
	public static boolean isCellularNetworkAvailable(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			if (activeNetwork != null) {
				// connected to the mobile provider's data plan
				return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Retrieves an {@link EditText} from Material's {@link TextInputLayout}
	 *
	 * @param inputLayout The {@link TextInputLayout}
	 * @return The {@link EditText} in the {@link TextInputLayout}
	 * @deprecated Use {@link TextInputLayout#getEditText()}
	 */
	@Deprecated
	public static EditText getEditText(TextInputLayout inputLayout) {
		return inputLayout.getEditText();
	}

	/**
	 * Retrieves the text from an {@link EditText} (or {@link com.google.android.material.textfield.TextInputEditText})
	 *
	 * @param editText The {@link EditText}
	 * @return The text of the {@link EditText}
	 */
	public static String getEditTextString(EditText editText) {
		return editText.getText().toString();
	}

	/**
	 * Retrieves the text from a {@link TextInputLayout}
	 *
	 * @param inputLayout The {@link TextInputLayout}
	 * @return The text of the {@link com.google.android.material.textfield.TextInputEditText} in {@link TextInputLayout}, or an empty string if no such {@link com.google.android.material.textfield.TextInputEditText} exists
	 */
	public static String getEditTextString(TextInputLayout inputLayout) {
		if (inputLayout.getEditText() != null) {
			return getEditTextString(inputLayout.getEditText());
		} else {
			Log.w(getTag(SharedHelper.class), "An EditText/TextInputEditText doesn't exist in the TextInputLayout.");
			return "";
		}
	}

	/**
	 * Retrieves the tag of a class. Useful for {@link android.util.Log}
	 *
	 * @param tagClass The class to retrieve the simple name of. Can be any Java class extending {@link Class}.
	 * @return The tag
	 */
	public static String getTag(Class tagClass) {
		return tagClass.getSimpleName();
	}

	/**
	 * Adds a new task to the Firebase Firestore database
	 *
	 * @param item The task item to add
	 * @param user The currently authenticated user
	 * @param fs   An instance of {@link FirebaseFirestore}
	 * @return The result.
	 */
	public static Task<DocumentReference> addTask(TaskItem item, FirebaseUser user, FirebaseFirestore fs) {
		return fs.collection("users/" + user.getUid() + "/todos").add(item);
	}

	/**
	 * Retrieves todos from the Firebase Firestore database
	 *
	 * @param user The currently authenticated user
	 * @param fs   An instance of {@link FirebaseFirestore}
	 * @return A collection reference
	 */
	public static CollectionReference getTasks(FirebaseUser user, FirebaseFirestore fs) {
		return fs.collection("users/" + user.getUid() + "/todos");
	}

	/**
	 * Removes a task from the Firebase Firestore database
	 *
	 * @param docID The document's ID
	 * @param user  The currently authenticated user
	 * @param fs    An instance of {@link FirebaseFirestore}
	 * @return The result of the deletion
	 */
	public static Task<Void> removeTask(String docID, FirebaseUser user, FirebaseFirestore fs) {
		return fs.document("users/" + user.getUid() + "/todos/" + docID).delete();
	}

	/**
	 * Shows a version dialog
	 *
	 * @param context The context to retrieve a {@link LayoutInflater} instance from & instantiate {@link AlertDialog.Builder}
	 */
	public static void showVersionDialog(Context context) {
		View versionDialogView = LayoutInflater.from(context).inflate(R.layout.version_dialog, null);
		ImageView appIconImageView = versionDialogView.findViewById(R.id.appIconImageView);
		TextView appNameTextView = versionDialogView.findViewById(R.id.appNameTextView);
		TextView appVersionTextView = versionDialogView.findViewById(R.id.appVersionTextView);
		try {
			appIconImageView.setImageDrawable(context.getPackageManager().getApplicationIcon(BuildConfig.APPLICATION_ID));
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(getTag(SharedHelper.class), "An error occurred while attempting to retrieve the app's icon:", e);
		}
		appNameTextView.setText(context.getApplicationInfo().loadLabel(context.getPackageManager()));
		appVersionTextView.setText(BuildConfig.VERSION_NAME);
		AlertDialog.Builder versionDialogBuilder = new AlertDialog.Builder(context);
		versionDialogBuilder
				.setView(versionDialogView)
				.show();
	}

	/**
	 * Used for setting up notification channels
	 * <p>NOTE: This will only work if the device is Android Oreo or later
	 *
	 * @param context The context
	 */
	public static void createNotificationChannels(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager =
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// Create a new list
			List<NotificationChannel> channels = new ArrayList<>();
			// Create another list for channel groups
			List<NotificationChannelGroup> channelGroups = new ArrayList<>();
			// Task updates notifications
			NotificationChannel todoUpdatesChannel = new NotificationChannel(context.getString(R.string.notification_channel_todo_updates_id), context.getString(R.string.notification_channel_todo_updates_title), NotificationManager.IMPORTANCE_HIGH);
			todoUpdatesChannel.setDescription(context.getString(R.string.notification_channel_todo_updates_desc));
			todoUpdatesChannel.setGroup(context.getString(R.string.notification_channel_group_todos_id));
			todoUpdatesChannel.enableLights(true);
			todoUpdatesChannel.setLightColor(Color.YELLOW);
			todoUpdatesChannel.enableVibration(true);
			todoUpdatesChannel.setShowBadge(true);
			channels.add(todoUpdatesChannel);

			// Weekly summary notifications
			NotificationChannel weeklySummaryChannel = new NotificationChannel(context.getString(R.string.notification_channel_weekly_summary_id), context.getString(R.string.notification_channel_weekly_summary_title), NotificationManager.IMPORTANCE_LOW);
			weeklySummaryChannel.setDescription(context.getString(R.string.notification_channel_weekly_summary_desc));
			weeklySummaryChannel.setGroup(context.getString(R.string.notification_channel_group_todos_id));
			weeklySummaryChannel.setShowBadge(true);
			channels.add(weeklySummaryChannel);

			// Syncing notifications
			NotificationChannel syncChannel = new NotificationChannel(context.getString(R.string.notification_channel_sync_id), context.getString(R.string.notification_channel_sync_title), NotificationManager.IMPORTANCE_LOW);
			syncChannel.setDescription(context.getString(R.string.notification_channel_sync_desc));
			syncChannel.setShowBadge(false);
			channels.add(syncChannel);

			// Update error notifications
			NotificationChannel updateErrorChannel = new NotificationChannel(context.getString(R.string.notification_channel_update_error_id), context.getString(R.string.notification_channel_update_error_title), NotificationManager.IMPORTANCE_HIGH);
			updateErrorChannel.setDescription(context.getString(R.string.notification_channel_update_error_desc));
			updateErrorChannel.setGroup(context.getString(R.string.notification_channel_group_updates_id));
			updateErrorChannel.setShowBadge(false);
			channels.add(updateErrorChannel);
			// Update status notifications
			NotificationChannel updateStatusChannel = new NotificationChannel(context.getString(R.string.notification_channel_update_status_id), context.getString(R.string.notification_channel_update_status_title), NotificationManager.IMPORTANCE_LOW);
			updateStatusChannel.setDescription(context.getString(R.string.notification_channel_update_status_desc));
			updateStatusChannel.setGroup(context.getString(R.string.notification_channel_group_updates_id));
			updateStatusChannel.setShowBadge(false);
			channels.add(updateStatusChannel);
			// Update complete notifications
			NotificationChannel updateCompleteChannel = new NotificationChannel(context.getString(R.string.notification_channel_update_complete_id), context.getString(R.string.notification_channel_update_complete_title), NotificationManager.IMPORTANCE_LOW);
			updateCompleteChannel.setDescription(context.getString(R.string.notification_channel_update_complete_desc));
			updateCompleteChannel.setGroup(context.getString(R.string.notification_channel_group_updates_id));
			updateCompleteChannel.setShowBadge(false);
			channels.add(updateCompleteChannel);
			// Update not available notifications
			NotificationChannel updateNotAvailableChannel = new NotificationChannel(context.getString(R.string.notification_channel_update_not_available_id), context.getString(R.string.notification_channel_update_not_available_title), NotificationManager.IMPORTANCE_DEFAULT);
			updateNotAvailableChannel.setDescription(context.getString(R.string.notification_channel_update_not_available_desc));
			updateNotAvailableChannel.setGroup(context.getString(R.string.notification_channel_group_updates_id));
			updateNotAvailableChannel.setShowBadge(false);
			channels.add(updateNotAvailableChannel);
			// Update available notifications
			NotificationChannel updateAvailableChannel = new NotificationChannel(context.getString(R.string.notification_channel_update_available_id), context.getString(R.string.notification_channel_update_available_title), NotificationManager.IMPORTANCE_HIGH);
			updateAvailableChannel.setDescription(context.getString(R.string.notification_channel_update_available_desc));
			updateAvailableChannel.setGroup(context.getString(R.string.notification_channel_group_updates_id));
			updateAvailableChannel.setShowBadge(false);
			channels.add(updateAvailableChannel);

			// Media playback notifications
			NotificationChannel playbackChannel = new NotificationChannel(context.getString(R.string.notification_channel_playback_id), context.getString(R.string.notification_channel_playback_title), NotificationManager.IMPORTANCE_LOW);
			playbackChannel.setDescription(context.getString(R.string.notification_channel_playback_desc));
			// We don't want to consider a playback notification to show a badge
			playbackChannel.setShowBadge(false);
			channels.add(playbackChannel);

			// Uncategorized notifications
			NotificationChannel uncategorisedChannel = new NotificationChannel(context.getString(R.string.notification_channel_uncategorised_id), context.getString(R.string.notification_channel_uncategorised_title), NotificationManager.IMPORTANCE_DEFAULT);
			uncategorisedChannel.setDescription(context.getString(R.string.notification_channel_uncategorised_desc));
			uncategorisedChannel.setShowBadge(true);
			channels.add(uncategorisedChannel);
			// Notification channel groups
			NotificationChannelGroup todoChannelGroup = new NotificationChannelGroup(context.getString(R.string.notification_channel_group_todos_id), context.getString(R.string.notification_channel_group_todos_title));
			channelGroups.add(todoChannelGroup);
			NotificationChannelGroup updatesChannelGroup = new NotificationChannelGroup(context.getString(R.string.notification_channel_group_updates_id), context.getString(R.string.notification_channel_group_updates_title));
			channelGroups.add(updatesChannelGroup);
			assert notificationManager != null;
			notificationManager.createNotificationChannelGroups(channelGroups);
			// Pass list to method
			notificationManager.createNotificationChannels(channels);

		}
	}

	/**
	 * An utility method to check for updates.
	 *
	 * @param context The context.
	 */
	public static void checkForUpdates(final Context context) {
		final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		final NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_update_status_id))
				.setSmallIcon(R.drawable.ic_notification_system_update_24dp)
				.setContentTitle(context.getString(R.string.notification_check_update))
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setProgress(100, 0, true)
				.setColor(ContextCompat.getColor(context, R.color.colorPrimary))
				.setOngoing(true);
		notificationManager.notify(NOTIFICATION_CHECK_FOR_UPDATES, notifyBuilder.build());
		final AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(context)
				.setUpdateFrom(UpdateFrom.JSON)
				.setUpdateJSON(context.getString(R.string.testing_changelog_url))
				.withListener(new AppUpdaterUtils.UpdateListener() {
					@Override
					public void onSuccess(Update update, Boolean updateAvailable) {
						if (update.getLatestVersionCode() == BuildConfig.VERSION_CODE && !updateAvailable) {
							// User is running latest version
							notifyBuilder.setContentTitle(context.getString(R.string.notification_no_updates))
									.setProgress(0, 0, false)
									.setOngoing(false);
							notificationManager.notify(NOTIFICATION_CHECK_FOR_UPDATES, notifyBuilder.build());
						} else {
							// New update
							Intent intentAction = new Intent(context, ActionButtonReceiver.class);

							intentAction.putExtra("action", ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER);
							intentAction.putExtra("downloadUrl", update.getUrlToDownload().toString());
							intentAction.putExtra("version", update.getLatestVersion());
							PendingIntent pIntentDownload = PendingIntent.getBroadcast(context, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
							notifyBuilder.setContentTitle(context.getString(R.string.notification_new_update_title))
									.setContentText(context.getString(R.string.notification_new_update_text, update.getLatestVersion()))
									.setProgress(0, 0, false)
									.setOngoing(false)
									.setChannelId(context.getString(R.string.notification_channel_update_available_id))
									.addAction(new NotificationCompat.Action(R.drawable.ic_download_24dp, "Download", pIntentDownload));
							notificationManager.notify(NOTIFICATION_CHECK_FOR_UPDATES, notifyBuilder.build());
						}
					}

					@Override
					public void onFailed(AppUpdaterError appUpdaterError) {
						switch (appUpdaterError) {
							case NETWORK_NOT_AVAILABLE:
								notifyBuilder.setContentTitle(context.getString(R.string.notification_updates_error_no_internet_title))
										.setContentText(context.getString(R.string.notification_updates_error_no_internet_text))
										.setSmallIcon(R.drawable.ic_wifi_strength_4_alert);
								break;
							case JSON_ERROR:
								notifyBuilder.setContentTitle(context.getString(R.string.notification_updates_error_not_found_title))
										.setContentText(context.getString(R.string.notification_updates_error_not_found_text))
										.setSmallIcon(R.drawable.ic_file_not_found_24dp);
						}
						Intent intentAction = new Intent(context, ActionButtonReceiver.class);

						//This is optional if you have more than one buttons and want to differentiate between two
						intentAction.putExtra("action", ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER);
						PendingIntent pIntentRetry = PendingIntent.getBroadcast(context, 2, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
						notificationManager.notify(NOTIFICATION_CHECK_FOR_UPDATES,
								notifyBuilder
										.setProgress(0, 0, false)
										.setOngoing(false)
										.setChannelId(context.getString(R.string.notification_channel_update_error_id))
										.setColor(ContextCompat.getColor(context, R.color.colorWarn))
										.addAction(new NotificationCompat.Action(R.drawable.ic_refresh_24dp, "Retry", pIntentRetry))
										.setStyle(new NotificationCompat.BigTextStyle())
										.build());
					}
				});
		appUpdaterUtils.start();
	}

	/**
	 * Checks if the permission is granted.
	 * Returns false if the permission isn't granted, true otherwise.
	 *
	 * @param permission The permission to check
	 * @param context    The context
	 * @return A boolean
	 */
	public static boolean checkPermission(String permission, Context context) {
		return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Dynamically creates a new ID for use with Android's notification manager
	 *
	 * @return The ID in question
	 */
	public int getDynamicId() {
		return atomicInteger.incrementAndGet();
	}

	/**
	 * Helper method to launch a URI
	 *
	 * @param context       The context
	 * @param uri           The URI to launch
	 * @param useCustomTabs Whether to use Chrome Custom Tabs
	 */
	public static void launchUri(Context context, Uri uri, boolean useCustomTabs) {
		if (useCustomTabs) {
			CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
			builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
					.addDefaultShareMenuItem()
					.setShowTitle(true);
			builder
					.build()
					.launchUrl(context, uri);
		} else {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(browserIntent);
		}
	}

	/**
	 * A newer implementation of the former <code>sendNotificationToUser</code> method.
	 * <p>
	 * This implementation reinforces that notifications are sent as requests to Cloud Firestore
	 * (which gets saved as a document under the <code>notificationRequests</code> collection) and are automatically sent to the associated topic or username.
	 * <p>
	 * This implementation also uses only 1 parameter to save on the amount of characters required to call the former method.
	 *
	 * @param request The notification request to send to Cloud Firestore
	 * @return A reference of the task
	 */
	public Task<DocumentReference> sendNotificationRequest(NotificationRequest request) {
		FirebaseFirestore fs = FirebaseFirestore.getInstance();
		return fs.collection("notificationRequests").add(request);
	}

	/**
	 * A newer implementation of the former <code>sendNotificationToUser</code> method.
	 * <p>
	 * This implementation reinforces that notifications are sent as requests to Cloud Firestore
	 * (which gets saved as a document under the <code>notificationRequests</code> collection) and are automatically sent to the associated topic or username.
	 * <p>
	 * This implementation also uses only 2 parameters to save on the amount of characters required to call the former method.
	 *
	 * @param fs      An instance of {@link FirebaseFirestore} (TIP: Use {@link FirebaseFirestore#getInstance()} to get the instance)
	 * @param request The notification request to send to Cloud Firestore
	 * @return A reference of the task
	 */
	public static Task<DocumentReference> sendNotificationRequest(FirebaseFirestore fs, NotificationRequest request) {
		return fs.collection("notificationRequests").add(request);
	}
}
