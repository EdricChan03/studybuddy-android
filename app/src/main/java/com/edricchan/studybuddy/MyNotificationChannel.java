package com.edricchan.studybuddy;

import android.content.Context;

@Deprecated
public class MyNotificationChannel {
	@Deprecated
	public String notificationTitle;
	@Deprecated
	public String notificationDesc;
	@Deprecated
	public String notificationId;
	@Deprecated
	public int index;

	@Deprecated
	public MyNotificationChannel(int notificationTitle, int notificationDesc, int notificationId, int index, Context context) {
		this.notificationTitle = context.getString(notificationTitle);
		this.notificationDesc = context.getString(notificationDesc);
		this.notificationId = context.getString(notificationId);
		this.index = index;
	}

	@Deprecated
	public MyNotificationChannel(CharSequence notificationTitle, String notificationDesc, String notificationId, int index) {
		this.notificationTitle = notificationTitle.toString();
		this.notificationDesc = notificationDesc;
		this.notificationId = notificationId;
		this.index = index;
	}
}
