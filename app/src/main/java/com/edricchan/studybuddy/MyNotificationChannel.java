package com.edricchan.studybuddy;

import android.content.Context;

/**
 * A class used for {@link SettingsActivity.NotificationPreferenceFragment#notificationChannels}
 */
public class MyNotificationChannel {
	public String notificationTitle;
	public String notificationDesc;
	public String notificationId;
	public int index;

	public MyNotificationChannel(int notificationTitle, int notificationDesc, int notificationId, int index, Context context) {
		this.notificationTitle = context.getString(notificationTitle);
		this.notificationDesc = context.getString(notificationDesc);
		this.notificationId = context.getString(notificationId);
		this.index = index;
	}

	public MyNotificationChannel(CharSequence notificationTitle, String notificationDesc, String notificationId, int index) {
		this.notificationTitle = notificationTitle.toString();
		this.notificationDesc = notificationDesc;
		this.notificationId = notificationId;
		this.index = index;
	}
}
