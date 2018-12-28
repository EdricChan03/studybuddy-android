package com.edricchan.studybuddy.interfaces

@Deprecated("")
data class NotificationData(
		val notificationChannelId: String? = "uncategorised",
		val notificationActions: List<NotificationAction>? = null
) {
	constructor() : this(notificationActions = null)

}
