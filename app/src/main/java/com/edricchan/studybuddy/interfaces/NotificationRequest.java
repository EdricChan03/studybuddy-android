package com.edricchan.studybuddy.interfaces;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringDef;

import java.util.ArrayList;
import java.util.List;

public class NotificationRequest {
	@StringDef({
			NOTIFICATION_PRIORITY_NORMAL,
			NOTIFICATION_PRIORITY_HIGH
	})
	public @interface NotificationPriority {
	}

	/**
	 * Indicates that the notification should have a normal priority
	 */
	public static final String NOTIFICATION_PRIORITY_NORMAL = "normal";
	/**
	 * Indicates that the notification should have a high priority
	 */
	public static final String NOTIFICATION_PRIORITY_HIGH = "high";
	/**
	 * Specifies the notification's actions
	 */
	private List<NotificationAction> notificationActions;
	/**
	 * Specifies the body/message of the notification
	 */
	private String notificationBody;
	/**
	 * Specifies the notification channel ID of the notification
	 * <p>Applicable for devices Android Oreo ({@link android.os.Build.VERSION_CODES#O}) and up.
	 */
	private String notificationChannelId;
	/**
	 * Specifies the colour/color of the notification
	 */
	private String notificationColor;
	/**
	 * Specifies the icon name of the notification
	 */
	private String notificationIcon;
	/**
	 * Specifies the priority of the notification
	 */
	@NotificationPriority
	private String notificationPriority;
	/**
	 * Specifies the title of the notification
	 */
	private String notificationTitle;
	/**
	 * Specifies the time-to-live (TTL) of the notification in milliseconds
	 */
	private long notificationTtl;
	/**
	 * Specifies the user ID or topic ID to send the notification to
	 */
	private String userOrTopic;

	public NotificationRequest() {
	}

	/**
	 * Retrieves the notification's actions
	 *
	 * @return The notification's actions
	 */
	public List<NotificationAction> getNotificationActions() {
		return this.notificationActions;
	}

	/**
	 * Retrieves the notification's body
	 *
	 * @return The notification's body
	 */
	public String getNotificationBody() {
		return this.notificationBody;
	}

	/**
	 * Retrieves the notification channel ID for use in Android Oreo and above
	 *
	 * @return The notification channel ID assigned to the notification
	 */
	public String getNotificationChannelId() {
		return this.notificationChannelId;
	}

	/**
	 * Retrieves the color of the notification
	 *
	 * @return The color of the notification in hexadecimal form
	 */
	public String getNotificationColor() {
		return this.notificationColor;
	}

	/**
	 * Retrieves the icon of the notification
	 *
	 * @return The name of the icon
	 */
	public String getNotificationIcon() {
		return this.notificationIcon;
	}

	/**
	 * Retrieves the priority of the notification
	 *
	 * @return The priority
	 */
	public String getNotificationPriority() {
		return this.notificationPriority;
	}

	/**
	 * Retrieves the title of the notification
	 *
	 * @return The title of the notification
	 */
	public String getNotificationTitle() {
		return this.notificationTitle;
	}

	/**
	 * Retrieves the time-to-live (TTL) of the notification
	 *
	 * @return The TTL of the notification in milliseconds
	 */
	public long getNotificationTtl() {
		return this.notificationTtl;
	}

	/**
	 * Retrieves the UID/topic ID
	 *
	 * @return The UID/topic ID
	 */
	public String getUserOrTopic() {
		return this.userOrTopic;
	}


	public static class Builder {
		private final NotificationRequest request;
		private List<NotificationAction> actions = new ArrayList<>();

		/**
		 * Creates a builder for a new notification request.
		 */
		public Builder() {
			this.request = new NotificationRequest();
		}

		/**
		 * Creates a builder for a notification request, but allows for an already set request
		 * to be passed in as a parameter.
		 *
		 * @param request The notification request
		 * @deprecated Use {@link Builder#Builder()} instead
		 */
		@Deprecated
		public Builder(NotificationRequest request) {
			this.request = request;
		}

		/**
		 * Clears all notification actions
		 *
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder clearNotificationActions() {
			actions.clear();
			return this;
		}

		/**
		 * Adds a notification action to the current list of notification actions
		 *
		 * @param action The action
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder addNotificationAction(NotificationAction action) {
			actions.add(action);
			return this;
		}

		/**
		 * Retrieves a notification action at the specified <code>index</code>
		 *
		 * @param index The index
		 * @return The notification action
		 * @deprecated Use {@link NotificationRequest#getNotificationActions()} and use the native <code>get</code> method
		 */
		@Deprecated
		public NotificationAction getNotificationAction(int index) {
			return actions.get(index);
		}

		/**
		 * Retrieves all notification actions
		 *
		 * @return The current list of notification actions
		 * @deprecated Use {@link NotificationRequest#getNotificationActions()} instead
		 */
		@Deprecated
		public List<NotificationAction> getNotificationActions() {
			return actions;
		}

		/**
		 * Sets the notification actions, overwriting the current list of notification actions
		 *
		 * @param actions A list of notification actions
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationActions(List<NotificationAction> actions) {
			this.actions = actions;
			return this;
		}

		/**
		 * Sets the body of the notification
		 *
		 * @param notificationBody The body
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationBody(String notificationBody) {
			request.notificationBody = notificationBody;
			return this;
		}

		/**
		 * Sets the notification channel ID of the notification (applicable for Android Oreo and higher)
		 *
		 * @param notificationChannelId A notification channel ID
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationChannelId(String notificationChannelId) {
			request.notificationChannelId = notificationChannelId;
			return this;
		}

		/**
		 * Sets the color of the notification
		 *
		 * @param notificationColor A hexadecimal color value
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationColor(String notificationColor) {
			request.notificationColor = notificationColor;
			return this;
		}

		/**
		 * Sets the icon of the notification
		 *
		 * @param notificationIcon A literal string of a drawable
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationIcon(String notificationIcon) {
			request.notificationIcon = notificationIcon;
			return this;
		}

		/**
		 * Sets the icon of the notification. Note that passing in a drawable will only convert it to a string
		 *
		 * @param context                  The context needed to get the string name of the drawable
		 * @param notificationIconDrawable A reference of a drawable
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link NotificationRequest.Builder#setNotificationIcon(String)} instead
		 */
		@Deprecated
		public Builder setNotificationIcon(Context context, @DrawableRes int notificationIconDrawable) {
			request.notificationIcon = context.getResources().getResourceEntryName(notificationIconDrawable);
			return this;
		}

		/**
		 * Sets the priority of the notification
		 *
		 * @param notificationPriority Either {@link NotificationRequest#NOTIFICATION_PRIORITY_NORMAL},
		 *                             or {@link NotificationRequest#NOTIFICATION_PRIORITY_HIGH}
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationPriority(@NotificationPriority String notificationPriority) {
			if (notificationPriority.equals(NOTIFICATION_PRIORITY_NORMAL) || notificationPriority.equals(NOTIFICATION_PRIORITY_HIGH)) {
				request.notificationPriority = notificationPriority;
			} else {
				// Invalid string!
				request.notificationPriority = NOTIFICATION_PRIORITY_NORMAL;
			}
			return this;
		}

		/**
		 * Sets the title of the notification
		 *
		 * @param notificationTitle The title
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationTitle(String notificationTitle) {
			request.notificationTitle = notificationTitle;
			return this;
		}

		/**
		 * Sets the time-to-live (TTL) of the notification
		 *
		 * @param notificationTtl The time-to-live, in milliseconds
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setNotificationTtl(int notificationTtl) {
			request.notificationTtl = notificationTtl;
			return this;
		}

		/**
		 * Sets the time-to-live (TTL) of the notification
		 *
		 * @param ttl The time-to-live, in milliseconds
		 * @return The builder object to allow for chaining of methods
		 * @deprecated Use {@link Builder#setNotificationTtl(int)} instead
		 */
		@Deprecated
		public Builder setTtl(int ttl) {
			request.notificationTtl = ttl;
			return this;
		}

		/**
		 * Sets the UID/topic ID to send the notification to
		 * <p>Note: To send to a topic, append the <code>userOrTopic</code> parameter with <code>topic_</code>.
		 *
		 * @param userOrTopic The UID/topic ID
		 * @return The builder object to allow for chaining of methods
		 */
		public Builder setUserOrTopic(String userOrTopic) {
			request.userOrTopic = userOrTopic;
			return this;
		}

		/**
		 * Checks if all values in the notification request are specified and returns the created notification request.
		 *
		 * @return The generated notification request
		 */
		public NotificationRequest create() throws RuntimeException {
			// Set the notification actions
			if (!actions.isEmpty()) {
				request.notificationActions = actions;
			}

			// Null checks to prevent values from being null on the document
			if (TextUtils.isEmpty(request.userOrTopic)) {
				throw new RuntimeException("Please supply a user or a topic!");
			}
			if (TextUtils.isEmpty(request.notificationBody)) {
				throw new RuntimeException("Please supply a body for the notification!");
			}
			if (TextUtils.isEmpty(request.notificationChannelId)) {
				// Use the default channel ID
				request.notificationChannelId = "uncategorised";
			}
			if (TextUtils.isEmpty(request.notificationColor)) {
				// Use the default colour/color
				request.notificationColor = "#3F51B5";
			}
			if (TextUtils.isEmpty(request.notificationIcon)) {
				// Use the default icon
				request.notificationIcon = "ic_studybuddy_pencil_icon_24dp";
			}
			if (TextUtils.isEmpty(request.notificationTitle)) {
				throw new RuntimeException("Please supply a title for the notification!");
			}
			if (TextUtils.isEmpty(request.notificationPriority)) {
				// Use the default priority
				request.notificationPriority = NOTIFICATION_PRIORITY_NORMAL;
			}
			if (request.notificationTtl == 0) {
				// Use the default TTL value (4 weeks)
				// Calculation: weeks * days * hours * minutes * seconds * milliseconds
				// Example: 1 week * 7 days * 24 hours * 60 minutes * 60 seconds * 1000 milliseconds
				request.notificationTtl = 4 * 7 * 24 * 60 * 60 * 1000L; // This should evaluate to 2419200000L
			}
			// And return it
			return request;
		}
	}
}
