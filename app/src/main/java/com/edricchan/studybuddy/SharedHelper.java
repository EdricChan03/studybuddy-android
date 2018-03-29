package com.edricchan.studybuddy;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SharedHelper {
	private Context mContext;
	public SharedHelper(Context context) {
		this.mContext = context;
	}
	/**
	 * Sends a notification to the user
	 * @param user The username
	 * @param message The message to send
	 * @param notificationChannelId The notification channel ID as a string
	 */
	public void sendNotificationToUser(String user, final String message, final String notificationChannelId) {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		CollectionReference notifications = db.collection("notificationRequests");

		Map<String, Object> notification = new HashMap<String, Object>();
		notification.put("username", user);
		notification.put("message", message);
		notification.put("notificationChannelId", notificationChannelId);
		notifications.add(notification);
	}

	/**
	 * Sends a notification to the user
	 * @param user The username
	 * @param message The message to send
	 * @param notificationChannelId The notification channel ID as a resource
	 */
	public void sendNotificationToUser(String user, final String message, final int notificationChannelId) {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		CollectionReference notifications = db.collection("notificationRequests");

		Map<String, Object> notification = new HashMap<String, Object>();
		notification.put("username", user);
		notification.put("message", message);
		notification.put("notificationChannelId", this.mContext.getString(notificationChannelId));
		notifications.add(notification);
	}

	/**
	 * Sends a notification to the user
	 * Note: The channel id will be assumed to be <code>uncategorised</code>
	 * @param user The username
	 * @param message The message to send
	 */
	public void sendNotificationToUser(String user, final String message) {
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		CollectionReference notifications = db.collection("notificationRequests");

		Map<String, Object> notification = new HashMap<String, Object>();
		notification.put("username", user);
		notification.put("message", message);
		// Assumr default notification channel (`uncategorised`)
		notification.put("notificationChannelId", this.mContext.getString(R.string.notification_channel_uncategorised_id));
		notifications.add(notification);
	}
}
