package com.edricchan.studybuddy.interfaces;

import java.util.List;

@Deprecated
public class NotificationData {
	private String mNotificationChannelId;
	private List<NotificationAction> mNotificationActions;
	public NotificationData() {

	}
	public NotificationData(String notificationChannelId, List<NotificationAction> notificationActions) {
		this.mNotificationChannelId = notificationChannelId;
		this.mNotificationActions = notificationActions;
	}
	public String getNotificationChannelId() {
		return this.mNotificationChannelId;
	}
	public List<NotificationAction> getNotificationActions() {
		return this.mNotificationActions;
	}
}
