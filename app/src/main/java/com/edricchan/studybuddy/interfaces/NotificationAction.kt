package com.edricchan.studybuddy.interfaces

import android.content.Context

import androidx.annotation.DrawableRes

/**
 * Specifies a notification request's action
 * @property actionIcon The action's icon
 * @property actionTitle The action's title
 * @property actionType The action's type
 */
data class NotificationAction(
		var actionIcon: String? = null,
		var actionTitle: String? = null,
		var actionType: String? = null
) {
	// TODO: Add support for creating multiple actions using the builder
	class Builder {
		private var action: NotificationAction? = null

		/**
		 * Creates a builder for a new notification action.
		 */
		constructor() {
			this.action = NotificationAction()
		}

		/**
		 * Creates a builder for a new notification action, but allows for an already set action
		 * to be passed in as a parameter.
		 *
		 * @param action The notification action
		 */
		@Deprecated("Use {@link Builder#Builder()} instead")
		constructor(action: NotificationAction) {
			this.action = action
		}

		/**
		 * Sets the icon of the action
		 *
		 * Note that this icon will not be shown on Android Nougat and up.
		 *
		 * @param actionIcon The name of the icon
		 * @return The builder object to allow for chaining of methods
		 */
		fun setActionIcon(actionIcon: String): Builder {
			action!!.actionIcon = actionIcon
			return this
		}

		/**
		 * Sets the icon of the action
		 *
		 * Note that this icon will not be shown on Android Nougat and up.
		 *
		 * @param context    The context to retrieve the icon from
		 * @param actionIcon The reference of the icon
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link Builder#setActionIcon(String)} instead")
		fun setActionIcon(context: Context, @DrawableRes actionIcon: Int): Builder {
			action!!.actionIcon = context.resources.getResourceEntryName(actionIcon)
			return this
		}

		/**
		 * Sets the title of the action
		 *
		 * @param actionTitle The title of the action
		 * @return The builder object to allow for chaining of methods
		 */
		fun setActionTitle(actionTitle: String): Builder {
			action!!.actionTitle = actionTitle
			return this
		}

		/**
		 * Sets the type of Intent to launch when clicked on
		 *
		 * @param actionType The action type
		 * @return The builder object to allow for chaining of methods
		 */
		fun setActionType(actionType: String): Builder {
			action!!.actionType = actionType
			return this
		}

		/**
		 * Returns the created notification action.
		 *
		 * @return The generated notification action
		 */
		fun create(): NotificationAction? {
			return action
		}
	}
}
