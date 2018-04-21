package com.edricchan.studybuddy.interfaces;

public class NotificationAction {
	private String mAction;
	private String mActionType;
	private String mActionIcon;

	public NotificationAction() {

	}
	public NotificationAction(NotificationAction notificationAction) {
		this.mAction = notificationAction.getAction();
		this.mActionType = notificationAction.getActionType();
		this.mActionIcon = notificationAction.getActionIcon();
	}
	public NotificationAction(String action, String actionType, String actionIcon) {
		this.mAction = action;
		this.mActionType = actionType;
		this.mActionIcon = actionIcon;
	}

	public String getAction() {
		return this.mAction;
	}

	public String getActionIcon() {
		return this.mActionIcon;
	}

	public String getActionType() {
		return this.mActionType;
	}
}
