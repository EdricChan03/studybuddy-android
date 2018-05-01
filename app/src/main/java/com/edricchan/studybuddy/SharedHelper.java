package com.edricchan.studybuddy;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.edricchan.studybuddy.interfaces.Notification;
import com.edricchan.studybuddy.interfaces.NotificationAction;
import com.edricchan.studybuddy.interfaces.NotificationData;
import com.edricchan.studybuddy.receiver.ActionButtonReceiver;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

	public static String getTag(Class tagClass) {
		return tagClass.getSimpleName();
	}

	public static void checkForUpdates(final Context context) {
		final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		final NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_app_updates_id))
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
									.addAction(new NotificationCompat.Action(R.drawable.ic_download_24dp, "Download", pIntentDownload));
							notificationManager.notify(NOTIFICATION_CHECK_FOR_UPDATES, notifyBuilder.build());
						}
					}

					@Override
					public void onFailed(AppUpdaterError appUpdaterError) {
						switch (appUpdaterError) {
							case NETWORK_NOT_AVAILABLE:
								Intent intentAction = new Intent(context, ActionButtonReceiver.class);

								//This is optional if you have more than one buttons and want to differentiate between two
								intentAction.putExtra("action", ACTION_NOTIFICATIONS_RETRY_CHECK_FOR_UPDATE_RECEIVER);
								PendingIntent pIntentRetry = PendingIntent.getBroadcast(context, 2, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
								notifyBuilder.setContentTitle(context.getString(R.string.notification_updates_error_no_internet_title))
										.setContentText(context.getString(R.string.notification_updates_error_no_internet_text))
										.setProgress(0, 0, false)
										.setSmallIcon(R.drawable.ic_wifi_strength_4_alert)
										.setOngoing(false)
										.setColor(ContextCompat.getColor(context, R.color.colorWarn))
										.setStyle(new NotificationCompat.BigTextStyle())
										.addAction(new NotificationCompat.Action(R.drawable.ic_refresh_24dp, "Retry", pIntentRetry));
								break;
						}
						notificationManager.notify(NOTIFICATION_CHECK_FOR_UPDATES, notifyBuilder.build());
					}
				});
		appUpdaterUtils.start();
	}

	/**
	 * Checks if the permission is granted and if it isn't, ask for permission
	 *
	 * @param permission The permission to check
	 * @param context    The context
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
		CollectionReference notifications = db.collection("notificationRequests");
		Notification notification = new Notification(user, message, body, color, new NotificationData(notificationChannelId, notificationActions));
		notifications.add(notification);
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
