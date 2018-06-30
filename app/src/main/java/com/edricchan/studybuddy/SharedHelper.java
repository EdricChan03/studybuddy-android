package com.edricchan.studybuddy;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.edricchan.studybuddy.interfaces.Notification;
import com.edricchan.studybuddy.interfaces.NotificationAction;
import com.edricchan.studybuddy.interfaces.NotificationData;
import com.edricchan.studybuddy.receiver.ActionButtonReceiver;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.concurrent.atomic.AtomicInteger;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class SharedHelper {
	/**
	 * Intent for notification settings action button for notifications
	 */
	public static final String ACTION_NOTIFICATIONS_SETTINGS_INTENT = "com.edricchan.studybuddy.intent.ACTION_NOTIFICATIONS_SETTINGS_INTENT";
	/**
	 * Broadcaster for starting download
	 */
	public static final String ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_START_DOWNLOAD_RECEIVER";
	/**
	 * Broadcaster for retrying check for updates
	 */
	public static final String ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER = "com.edricchan.studybuddy.receiver.ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER";
	/**
	 * Action icon for settings
	 */
	public static final String ACTION_SETTINGS_ICON = "settings";
	/**
	 * Action icon for notification
	 */
	public static final String ACTION_NOTIFICATION_ICON = "notification";
	/**
	 * Action icon for mark as done
	 */
	public static final String ACTION_MARK_AS_DONE_ICON = "mark_as_done";
	// IDs for notifications
	/**
	 * ID for checking for updates
	 */
	public static final int NOTIFICATION_CHECK_FOR_UPDATES = 0;
	/**
	 * ID for mMediaPlayer notification
	 */
	public static final int NOTIFICATION_MEDIA = 1;
	// Context
	private Context mContext;
	// Since IDs 0 and 1 have been taken, use 2
	private int dynamicId = 2;
	private AtomicInteger atomicInteger = new AtomicInteger(dynamicId);

	public SharedHelper(Context context) {
		this.mContext = context;
	}

	public SharedHelper() {
	}

	/**
	 * @param datePicker
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
	 * @deprecated Use {@link SharedHelper#isNetworkAvailable(Context)}  instead
	 * @param context The context
	 * @return A boolean
	 */
	public static boolean isNetworkUnavailable(Context context) {
		return !isNetworkAvailable(context);
	}

	/**
	 * Retrieves an {@link EditText} from Google Android Material's {@link TextInputLayout}
	 *
	 * @param inputLayout The {@link TextInputLayout} in question
	 * @return The {@link EditText} in the {@link TextInputLayout}
	 */
	public static EditText getEditText(TextInputLayout inputLayout) {
		return inputLayout.getEditText();
	}

	/**
	 * Retrieves the text from an {@link EditText} (or {@link com.google.android.material.textfield.TextInputEditText})
	 *
	 * @param editText The {@link EditText} in question
	 * @return The text of the {@link EditText}
	 */
	public static String getEditTextString(EditText editText) {
		return editText.getText().toString();
	}

	/**
	 * Retrieves the text from a {@link TextInputLayout}
	 *
	 * @param inputLayout The {@link TextInputLayout} in question
	 * @return The text of the {@link EditText} in {@link TextInputLayout}
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
	 * @param tagClass The class in question. Accepts any valid Java class (including Android activities, fragments, etc.)
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
	public static Task<DocumentReference> addTodo(TaskItem item, FirebaseUser user, FirebaseFirestore fs) {
		return fs.collection("users/" + user.getUid() + "/todos").add(item);
	}

	/**
	 * Retrieves todos from the Firebase Firestore database
	 *
	 * @param user The currently authenticated user
	 * @param fs   An instance of {@link FirebaseFirestore}
	 * @return A collection reference
	 */
	public static CollectionReference getTodos(FirebaseUser user, FirebaseFirestore fs) {
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
	public static Task<Void> removeTodo(String docID, FirebaseUser user, FirebaseFirestore fs) {
		return fs.document("users/" + user.getUid() + "/todos/" + docID).delete();
	}

	/**
	 * Used for setting up notification channels
	 * NOTE: This will only work if the device is Android Oreo or later
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
			playbackChannel.setShowBadge(true);
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
							intentAction.putExtra("version", update.getLatestVersion().toString());
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
		if (ContextCompat.checkSelfPermission(context, permission)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission is not granted
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Parses the string (<code>s</code>) into an object (<code>clazz</code>)
	 *
	 * @param s     The string to parse
	 * @param clazz The class type to parse the string to
	 * @param <T>   Any
	 * @return The constructor
	 * @throws Exception
	 */
	public static <T> T parseObjectFromString(String s, Class<T> clazz) throws Exception {
		return clazz.getConstructor(new Class[]{String.class}).newInstance(s);
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
	 * Creates a notification action
	 *
	 * @return {@link NotificationAction} The notification action
	 */
	private List<NotificationAction> addDefaultNotificationActions() {
		List<NotificationAction> notificationActionList = new ArrayList<>();
		notificationActionList.add(new NotificationAction("settings", "Configure notifications", ACTION_NOTIFICATIONS_SETTINGS_INTENT));
		return notificationActionList;
	}

	/**
	 * Sends a notification to the user
	 *
	 * @param user                  The username
	 * @param message               The message to send
	 * @param notificationChannelId The notification channel ID as a string
	 * @param notificationActions   The notification actions
	 */
	public void sendNotificationToUser(String user, String message, String body, String color, String notificationChannelId, List<NotificationAction> notificationActions) {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		final CollectionReference notifications = db.collection("notificationRequests");
		Notification notification = new Notification(user, message, body, color, new NotificationData(notificationChannelId, notificationActions));
		notifications.add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
			@Override
			public void onSuccess(DocumentReference documentReference) {
				documentReference.update("id", documentReference.getId());
			}
		});
	}

	/**
	 * Sends a notification to the user
	 *
	 * @param user                  The username
	 * @param message               The message to send
	 * @param notificationChannelId The notification channel ID as a string
	 */
	public void sendNotificationToUser(String user, String message, String notificationChannelId) {
		this.sendNotificationToUser(user, message, "", notificationChannelId, "#9c27b0", this.addDefaultNotificationActions());
	}

	/**
	 * Sends a notification to the user
	 *
	 * @param user                  The username
	 * @param message               The message to send
	 * @param body                  The body to send
	 * @param notificationChannelId The notification channel ID as a string
	 */
	public void sendNotificationToUserWithBody(String user, String message, String body, String notificationChannelId) {
		this.sendNotificationToUser(user, message, body, "#9c27b0", notificationChannelId, this.addDefaultNotificationActions());
	}

	/**
	 * Sends a notification to the user
	 *
	 * @param user                  The username
	 * @param message               The message to send
	 * @param notificationChannelId The notification channel ID as a resource
	 */
	public void sendNotificationToUser(String user, String message, int notificationChannelId) {
		this.sendNotificationToUser(user, message, "", "#9c27b0", mContext.getString(notificationChannelId), this.addDefaultNotificationActions());
	}

	/**
	 * Sends a notification to the user
	 *
	 * @param user                  The username
	 * @param message               The message to send
	 * @param body                  The body to send
	 * @param notificationChannelId The notification channel ID as a resource
	 */
	public void sendNotificationToUserWithBody(String user, String message, String body, int notificationChannelId) {
		this.sendNotificationToUser(user, message, body, "#9c27b0", mContext.getString(notificationChannelId), this.addDefaultNotificationActions());
	}

	/**
	 * Sends a notification to the user
	 * Note: The channel id will be assumed to be <code>uncategorised</code>
	 *
	 * @param user    The username
	 * @param message The message to send
	 */
	public void sendNotificationToUser(String user, String message) {
		this.sendNotificationToUser(user, message, "", "#9c27b0", mContext.getString(R.string.notification_channel_uncategorised_id), this.addDefaultNotificationActions());
	}

	/**
	 * Sends a notification to the user
	 * Note: The channel id will be assumed to be <code>uncategorised</code>
	 *
	 * @param user    The username
	 * @param message The message to send
	 * @param body    The body to send
	 */
	public void sendNotificationToUserWithBody(String user, String message, String body) {
		this.sendNotificationToUser(user, message, body, "#9c27b0", mContext.getString(R.string.notification_channel_uncategorised_id), this.addDefaultNotificationActions());
	}
}
