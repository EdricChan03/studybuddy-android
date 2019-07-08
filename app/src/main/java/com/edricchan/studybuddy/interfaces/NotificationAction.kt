package com.edricchan.studybuddy.interfaces

/**
 * Specifies a notification request's action
 * @property actionIcon The action's icon
 * @property actionTitle The action's title
 * @property actionType The action's type
 */
data class NotificationAction(
		val actionTitle: String? = null,
		val actionIcon: String? = null,
		val actionType: String? = null
) {
	private constructor(builder: Builder) : this(
			builder.actionTitle,
			builder.actionIcon,
			builder.actionType
	)

	companion object {
		/**
		 * Creates a [NotificationAction] using a [Builder] (with support for inlined setting of variables)
		 * @return The created [NotificationAction]
		 */
		inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
	}

	// TODO: Add support for creating multiple actions using the builder
	/**
	 * Builder object to simplify the creation of a [NotificationAction] object
	 */
	class Builder {
		/**
		 * The notification action's title
		 */
		var actionTitle: String? = null
		/**
		 * The notification action's icon
		 */
		var actionIcon: String? = null
		/**
		 * The notification action's intent type
		 */
		var actionType: String? = null

		/**
		 * Returns the created [NotificationAction]
		 * @return The created [NotificationAction]
		 */
		fun build() = NotificationAction(this)
	}
}
