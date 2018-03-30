package com.edricchan.studybuddy.interfaces;

public class NotificationAction {
	private String mAction;
	private String mActionType;
	public NotificationAction(String action, String actionType) {
		this.mAction = action;
		this.mActionType = actionType;
	}
	public String getAction() {
		return this.mAction;
	}
	public String getActionType() {
		return this.mActionType;
	}
}
