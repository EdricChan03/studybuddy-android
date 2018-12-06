package com.edricchan.studybuddy.interfaces;

import android.content.Context;

import androidx.annotation.DrawableRes;

public class NotificationAction {
	/**
	 * Specifies the action's icon
	 */
	private String actionIcon;
	/**
	 * Specifies the action's title
	 */
	private String actionTitle;
	/**
	 * Specifies the action's Intent to launch
	 */
	private String actionType;

	public NotificationAction() {

	}

	/**
	 * Retrives the action's icon
	 * @return The action's icon's name
	 */
	public String getActionIcon() {
		return this.actionIcon;
	}

	/**
	 * Retrieves the action's title
	 * @return The action's title
	 */
	public String getActionTitle() {
		return this.actionTitle;
	}

	/**
	 * Retrieves the action's type of Intent to launch when clicked on
	 * @return The action's type
	 */
	public String getActionType() {
		return this.actionType;
	}

	// TODO: Add support for creating multiple actions using the builder
	public static class Builder {
		private NotificationAction action;

		/**
		 * Creates a builder for a new notification action.
		 */
		public Builder() {
			this.action = new NotificationAction();
		}

		/**
		 * Creates a builder for a new notification action, but allows for an already set action
		 * to be passed in as a parameter.
		 *
		 * @param action The notification action
		 * @deprecated Use {@link Builder#Builder()} instead
		 */
		public Builder(NotificationAction action) {
			this.action = action;
		}

		/**
		 * Sets the icon of the action
		 * <p>Note that this icon will not be shown on Android Nougat and up.
		 *
		 * @param actionIcon The name of the icon
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setActionIcon(String actionIcon) {
			action.actionIcon = actionIcon;
			return this;
		}

		/**
		 * Sets the icon of the action
		 * <p>Note that this icon will not be shown on Android Nougat and up.
		 *
		 * @param context    The context to retrieve the icon from
		 * @param actionIcon The reference of the icon
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setActionIcon(String)} instead
		 */
		@Deprecated
		public Builder setActionIcon(Context context, @DrawableRes int actionIcon) {
			action.actionIcon = context.getResources().getResourceEntryName(actionIcon);
			return this;
		}

		/**
		 * Sets the title of the action
		 *
		 * @param actionTitle The title of the action
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setActionTitle(String actionTitle) {
			action.actionTitle = actionTitle;
			return this;
		}

		/**
		 * Sets the type of Intent to launch when clicked on
		 *
		 * @param actionType The action type
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setActionType(String actionType) {
			action.actionType = actionType;
			return this;
		}

		/**
		 * Returns the created notification action.
		 *
		 * @return The generated notification action
		 */
		public NotificationAction create() {
			return action;
		}
	}
}
