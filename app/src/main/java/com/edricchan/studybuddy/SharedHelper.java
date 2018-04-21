package com.edricchan.studybuddy;

import android.app.Activity;
import android.content.Context;

import com.edricchan.studybuddy.interfaces.Notification;
import com.edricchan.studybuddy.interfaces.NotificationAction;
import com.edricchan.studybuddy.interfaces.NotificationData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedHelper {
	/**
	 * Intent for notification settings action button for notifications
	 */
	public static final String ACTION_NOTIFICATIONS_SETTINGS = "com.edricchan.studybuddy.intent.ACTION_NOTIFICATIONS_SETTINGS";
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
	private Context mContext;
	private int dynamicId = 0;
	private AtomicInteger atomicInteger = new AtomicInteger(0);

	public SharedHelper(Context context) {
		this.mContext = context;
	}

	public SharedHelper() {
	}

	public static String getTag(Class tagClass) {
		return tagClass.getSimpleName();
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
		notificationActionList.add(new NotificationAction("settings", "Configure notifications", ACTION_NOTIFICATIONS_SETTINGS));
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
