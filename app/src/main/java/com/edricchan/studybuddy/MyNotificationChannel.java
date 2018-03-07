package com.edricchan.studybuddy;

/**
 * A class used for {@link SettingsActivity.NotificationPreferenceFragment#notificationChannels}
 */
public class MyNotificationChannel {
	private int notificationTitle;
	private int notificationDesc;
	private int notificationId;
	private int index;

	public MyNotificationChannel(int notificationTitle, int notificationDesc, int notificationId, int index) {
		this.notificationTitle = notificationTitle;
		this.notificationDesc = notificationDesc;
		this.notificationId = notificationId;
		this.index = index;
	}

	public int getNotificationTitle() {
		return this.notificationTitle;
	}

	public int getNotificationDesc() {
		return this.notificationDesc;
	}

	public int getIndex() {
		return this.index;
	}

	public int getNotificationId() {
		return this.notificationId;
	}
}
