package com.edricchan.studybuddy.providers.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.edricchan.studybuddy.MainActivity;
import com.edricchan.studybuddy.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class StudyBuddyMessagingService extends FirebaseMessagingService {
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		if (remoteMessage != null) {
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
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
			}
			if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
				// Check if the data of the remote message has a notificationChannelId as well as if the device is Android Oreo and up
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					if (remoteMessage.getData().containsKey("notificationChannelId")) {
						System.out.println("Data exists");
						// Checks if the specified notification channel ID exists
						if (manager.getNotificationChannel(remoteMessage.getData().get("notificationChannelId")) == null) {
							// Create a notification channel
							NotificationChannel notificationChannel = new NotificationChannel(remoteMessage.getData().get("notificationChannelId"), "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
							notificationChannel.setDescription("This notification channel was generated since the channel ID for the notification doesn't exist.");
							manager.createNotificationChannel(notificationChannel);
						}
						builder.setChannelId(remoteMessage.getData().get("notificationChannelId"));
					} else {
						builder.setChannelId(getString(R.string.notification_channel_uncategorised_id));
					}
				}
				// Check if the data of the remote message has a notificationActions
				/* TODO: Add support for notification actions
				if (remoteMessage.getData().containsKey("notificationActions")) {
					List<NotificationAction> notificationActions = (List<NotificationAction>)
							remoteMessage.getData().get("notificationActions").toString();
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
						switch (notificationAction.getActionType()) {
							case SharedHelper.ACTION_NOTIFICATIONS_SETTINGS:
								intent = new Intent(this, SettingsActivity.class);
								intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.NotificationPreferenceFragment.class.getName());
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								notificationPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
								break;
						}
						builder.addAction(new NotificationCompat.Action(icon, notificationAction.getAction(), notificationPendingIntent));
					}
				}
				*/
			}
			Intent mainActivityIntent = new Intent(this, MainActivity.class);
			mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, PendingIntent.FLAG_ONE_SHOT);
			builder.setContentIntent(mainPendingIntent);
			if (!remoteMessage.getNotification().getIcon().isEmpty()) {
				builder.setSmallIcon(getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName()));
			} else {
				builder.setSmallIcon(R.drawable.ic_studybuddy_notification_icon);
			}
			if (!remoteMessage.getNotification().getColor().isEmpty()) {
				builder.setColor(Color.parseColor(remoteMessage.getNotification().getColor()));
			} else {
				builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
			}
			manager.notify(0, builder.build());
		} else {
			System.out.println("Oops! No remote message! :(");
		}
	}
}
