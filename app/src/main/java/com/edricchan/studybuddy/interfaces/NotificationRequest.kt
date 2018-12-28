package com.edricchan.studybuddy.interfaces

import android.content.Context
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.annotation.StringDef
import java.util.*

/**
 * Specifies a notification request to be sent to a device/user/topic
 * @property notificationActions The notification's actions as a [List<NotificationAction>]
 * @property notificationBody The body/message of the notification
 * @property notificationChannelId The notification channel ID of the notification
 *                                 Applicable for devices running Android Oreo ([android.os.Build.VERSION_CODES.O]) and up.
 * @property notificationColor The color/colour of the notification
 * @property notificationIcon The icon of the notification
 * @property notificationPriority The priority of the notification
 * @property notificationTitle The title of the notification
 * @property notificationTtl The time-to-live (TTL) of the notification
 * @property userOrTopic The user ID or the topic ID to send the notification to
 */
data class NotificationRequest(
		var notificationActions: List<NotificationAction>? = null,
		var notificationBody: String? = null,
		var notificationChannelId: String? = "uncategorised",
		var notificationColor: String? = "#3f51b5",
		var notificationIcon: String? = null,
		@NotificationPriority var notificationPriority: String? = null,
		var notificationTitle: String? = null,
		var notificationTtl: Long? = null,
		var userOrTopic: String? = null
) {

	@StringDef(NOTIFICATION_PRIORITY_NORMAL, NOTIFICATION_PRIORITY_HIGH)
	annotation class NotificationPriority


	class Builder {
		private val request: NotificationRequest
		private var actions: MutableList<NotificationAction> = ArrayList()

		/**
		 * Retrieves all notification actions
		 *
		 * @return The current list of notification actions
		 */
		@Deprecated("Use {@link NotificationRequest#getNotificationActions()} instead")
		val notificationActions: List<NotificationAction>
			get() = actions

		/**
		 * Creates a builder for a new notification request.
		 */
		constructor() {
			this.request = NotificationRequest()
		}

		/**
		 * Creates a builder for a notification request, but allows for an already set request
		 * to be passed in as a parameter.
		 *
		 * @param request The notification request
		 */
		@Deprecated("Use {@link Builder#Builder()} instead")
		constructor(request: NotificationRequest) {
			this.request = request
		}

		/**
		 * Clears all notification actions
		 *
		 * @return The builder object to allow for chaining of methods
		 */
		fun clearNotificationActions(): Builder {
			actions.clear()
			return this
		}

		/**
		 * Adds a notification action to the current list of notification actions
		 *
		 * @param action The action
		 * @return The builder object to allow for chaining of methods
		 */
		fun addNotificationAction(action: NotificationAction): Builder {
			actions.add(action)
			return this
		}

		/**
		 * Retrieves a notification action at the specified `index`
		 *
		 * @param index The index
		 * @return The notification action
		 */
		@Deprecated("Use {@link NotificationRequest#getNotificationActions()} and use the native <code>get</code> method")
		fun getNotificationAction(index: Int): NotificationAction {
			return actions[index]
		}

		/**
		 * Sets the notification actions, overwriting the current list of notification actions
		 *
		 * @param actions A list of notification actions
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationActions(actions: MutableList<NotificationAction>): Builder {
			this.actions = actions
			return this
		}

		/**
		 * Sets the body of the notification
		 *
		 * @param notificationBody The body
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationBody(notificationBody: String): Builder {
			request.notificationBody = notificationBody
			return this
		}

		/**
		 * Sets the notification channel ID of the notification (applicable for Android Oreo and higher)
		 *
		 * @param notificationChannelId A notification channel ID
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationChannelId(notificationChannelId: String): Builder {
			request.notificationChannelId = notificationChannelId
			return this
		}

		/**
		 * Sets the color of the notification
		 *
		 * @param notificationColor A hexadecimal color value
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationColor(notificationColor: String): Builder {
			request.notificationColor = notificationColor
			return this
		}

		/**
		 * Sets the icon of the notification
		 *
		 * @param notificationIcon A literal string of a drawable
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationIcon(notificationIcon: String): Builder {
			request.notificationIcon = notificationIcon
			return this
		}

		/**
		 * Sets the icon of the notification. Note that passing in a drawable will only convert it to a string
		 *
		 * @param context                  The context needed to get the string name of the drawable
		 * @param notificationIconDrawable A reference of a drawable
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link NotificationRequest.Builder#setNotificationIcon(String)} instead")
		fun setNotificationIcon(context: Context, @DrawableRes notificationIconDrawable: Int): Builder {
			request.notificationIcon = context.resources.getResourceEntryName(notificationIconDrawable)
			return this
		}

		/**
		 * Sets the priority of the notification
		 *
		 * @param notificationPriority Either [NotificationRequest.NOTIFICATION_PRIORITY_NORMAL],
		 * or [NotificationRequest.NOTIFICATION_PRIORITY_HIGH]
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationPriority(@NotificationPriority notificationPriority: String): Builder {
			if (notificationPriority == NOTIFICATION_PRIORITY_NORMAL || notificationPriority == NOTIFICATION_PRIORITY_HIGH) {
				request.notificationPriority = notificationPriority
			} else {
				// Invalid string!
				request.notificationPriority = NOTIFICATION_PRIORITY_NORMAL
			}
			return this
		}

		/**
		 * Sets the title of the notification
		 *
		 * @param notificationTitle The title
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationTitle(notificationTitle: String): Builder {
			request.notificationTitle = notificationTitle
			return this
		}

		/**
		 * Sets the time-to-live (TTL) of the notification
		 *
		 * @param notificationTtl The time-to-live, in milliseconds
		 * @return The builder object to allow for chaining of methods
		 */
		fun setNotificationTtl(notificationTtl: Int): Builder {
			request.notificationTtl = notificationTtl.toLong()
			return this
		}

		/**
		 * Sets the time-to-live (TTL) of the notification
		 *
		 * @param ttl The time-to-live, in milliseconds
		 * @return The builder object to allow for chaining of methods
		 */
		@Deprecated("Use {@link Builder#setNotificationTtl(int)} instead")
		fun setTtl(ttl: Int): Builder {
			request.notificationTtl = ttl.toLong()
			return this
		}

		/**
		 * Sets the UID/topic ID to send the notification to
		 *
		 * Note: To send to a topic, append the `userOrTopic` parameter with `topic_`.
		 *
		 * @param userOrTopic The UID/topic ID
		 * @return The builder object to allow for chaining of methods
		 */
		fun setUserOrTopic(userOrTopic: String): Builder {
			request.userOrTopic = userOrTopic
			return this
		}

		/**
		 * Checks if all values in the notification request are specified and returns the created notification request.
		 *
		 * @return The generated notification request
		 */
		@Throws(RuntimeException::class)
		fun create(): NotificationRequest {
			// Set the notification actions
			if (!actions.isEmpty()) {
				request.notificationActions = actions
			}

			// Null checks to prevent values from being null on the document
			if (TextUtils.isEmpty(request.userOrTopic)) {
				throw RuntimeException("Please supply a user or a topic!")
			}
			if (TextUtils.isEmpty(request.notificationBody)) {
				throw RuntimeException("Please supply a body for the notification!")
			}
			if (TextUtils.isEmpty(request.notificationChannelId)) {
				// Use the default channel ID
				request.notificationChannelId = "uncategorised"
			}
			if (TextUtils.isEmpty(request.notificationColor)) {
				// Use the default colour/color
				request.notificationColor = "#3F51B5"
			}
			if (TextUtils.isEmpty(request.notificationIcon)) {
				// Use the default icon
				request.notificationIcon = "ic_studybuddy_pencil_icon_24dp"
			}
			if (TextUtils.isEmpty(request.notificationTitle)) {
				throw RuntimeException("Please supply a title for the notification!")
			}
			if (TextUtils.isEmpty(request.notificationPriority)) {
				// Use the default priority
				request.notificationPriority = NOTIFICATION_PRIORITY_NORMAL
			}
			if (request.notificationTtl == 0L) {
				// Use the default TTL value (4 weeks)
				// Calculation: weeks * days * hours * minutes * seconds * milliseconds
				// Example: 1 week * 7 days * 24 hours * 60 minutes * 60 seconds * 1000 milliseconds
				request.notificationTtl = 4 * 7 * 24 * 60 * 60 * 1000L // This should evaluate to 2419200000L
			}
			// And return it
			return request
		}
	}

	companion object {

		/**
		 * Indicates that the notification should have a normal priority
		 */
		const val NOTIFICATION_PRIORITY_NORMAL = "normal"
		/**
		 * Indicates that the notification should have a high priority
		 */
		const val NOTIFICATION_PRIORITY_HIGH = "high"
	}
}
