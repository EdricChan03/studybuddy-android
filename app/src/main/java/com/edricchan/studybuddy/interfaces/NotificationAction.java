package com.edricchan.studybuddy.interfaces;

public class NotificationAction {
	public String action;
	public String actionType;
	public String actionIcon;

	public NotificationAction() {

	}
	public NotificationAction(NotificationAction notificationAction) {
		this.action = notificationAction.getAction();
		this.actionType = notificationAction.getActionType();
		this.actionIcon = notificationAction.getActionIcon();
	}
	public NotificationAction(String action, String actionType, String actionIcon) {
		this.action = action;
		this.actionType = actionType;
		this.actionIcon = actionIcon;
	}

	public String getAction() {
		return this.action;
	}

	public String getActionIcon() {
		return this.actionIcon;
	}

	public String getActionType() {
		return this.actionType;
	}
}
