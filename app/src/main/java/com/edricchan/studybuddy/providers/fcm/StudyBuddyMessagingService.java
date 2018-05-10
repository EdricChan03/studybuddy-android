package com.edricchan.studybuddy.providers.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.edricchan.studybuddy.MainActivity;
import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SettingsActivity;
import com.edricchan.studybuddy.SharedHelper;
import com.edricchan.studybuddy.interfaces.Notification;
import com.edricchan.studybuddy.interfaces.NotificationAction;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudyBuddyMessagingService extends FirebaseMessagingService {
	private SharedHelper sharedHelper = new SharedHelper(this);
	// To be used for Android's Log
	private String TAG = sharedHelper.getTag(this.getClass());
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		if (remoteMessage != null) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_uncategorised_id));
			if (remoteMessage.getNotification() != null) {
				// Check if remote message has a title
				if (!remoteMessage.getNotification().getTitle().isEmpty()) {
					builder.setContentTitle(remoteMessage.getNotification().getTitle());
					builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
				}
				// Check if the remote message has a message body
				if (!remoteMessage.getNotification().getBody().isEmpty()) {
					builder.setContentText(remoteMessage.getNotification().getBody());
				}
				// Check if icon property exists
				if (!remoteMessage.getNotification().getIcon().isEmpty()) {
					// Use the icon
					builder.setSmallIcon(getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName()));
				} else {
					// Use the default icon
					builder.setSmallIcon(R.drawable.ic_notification_studybuddy_24dp);
				}
				// Check if color property exists
				if (!remoteMessage.getNotification().getColor().isEmpty()) {
					// Use the color
					builder.setColor(Color.parseColor(remoteMessage.getNotification().getColor()));
				} else {
					// Use the default color
					builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
				}
			}
			if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
				// Check if the data of the remote message has a notificationChannelId as well as if the device is Android Oreo and up
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					/*
					Check if notificationChannelID exists
					Note that since the notification channel ID is already defined in the Builder above, there's no need for an else statement
					 */
					if (remoteMessage.getData().containsKey("notificationChannelId")) {
						// Checks if the specified notification channel ID exists
						if (manager.getNotificationChannel(remoteMessage.getData().get("notificationChannelId")) == null) {
							// Create a notification channel
							NotificationChannel notificationChannel = new NotificationChannel(remoteMessage.getData().get("notificationChannelId"), "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
							notificationChannel.setDescription("This notification channel was generated since the channel ID for the notification doesn't exist.");
							manager.createNotificationChannel(notificationChannel);
						}
						// Set the channel ID
						builder.setChannelId(remoteMessage.getData().get("notificationChannelId"));
					}
				}
				// Check if the data of the remote message has a notificationActions
				if (remoteMessage.getData().containsKey("notificationActions")) {
					Log.d(TAG, "notificationActions: " + remoteMessage.getData().get("notificationActions"));
					List<NotificationAction> notificationActions = new ArrayList<>();
					try {
						Gson gson = new Gson();
						NotificationAction[] remoteNotificationActions = gson.fromJson(remoteMessage.getData().get("notificationActions"), NotificationAction[].class);
						notificationActions = Arrays.asList(remoteNotificationActions);
						Log.d(TAG, "Size: " + remoteNotificationActions.length);
						Log.d(TAG, "JSON: " + remoteNotificationActions.toString());
						Log.d(TAG, "NotificationAction#action: " + remoteNotificationActions[0].getAction());
					} catch (Exception e) {
						e.printStackTrace();
						Crashlytics.logException(e);
					}
					for (NotificationAction notificationAction : notificationActions) {
						int icon = 0;
						// Initial intent
						Intent intent;
						// Notification pending intent
						PendingIntent notificationPendingIntent = null;
						switch (notificationAction.getActionIcon()) {
							case SharedHelper.ACTION_MARK_AS_DONE_ICON:
								break;
							case SharedHelper.ACTION_NOTIFICATION_ICON:
								icon = R.drawable.ic_notifications_24dp;
								break;
							case SharedHelper.ACTION_SETTINGS_ICON:
								icon = R.drawable.ic_settings_24dp;
								break;
						}
						switch (notificationAction.actionType) {
							case SharedHelper.ACTION_NOTIFICATIONS_SETTINGS_INTENT:
								intent = new Intent(this, SettingsActivity.class);
								intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.NotificationPreferenceFragment.class.getName());
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								notificationPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
								break;
						}
						builder.addAction(new NotificationCompat.Action(icon, notificationAction.getAction(), notificationPendingIntent));
					}
				}
			}
			Intent mainActivityIntent = new Intent(this, MainActivity.class);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_ONE_SHOT);
			builder.setContentIntent(mainPendingIntent);
			manager.notify(sharedHelper.getDynamicId(), builder.build());
		}
	}
}
