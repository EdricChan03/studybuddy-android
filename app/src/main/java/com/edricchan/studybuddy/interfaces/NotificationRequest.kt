package com.edricchan.studybuddy.interfaces

import androidx.annotation.StringDef

/**
 * Specifies a notification request to be sent to a device/user/topic
 * @property notificationActions The notification's actions as a [List<NotificationAction>]
 * @property notificationBody The body/message of the notification
 * @property notificationChannelId The notification channel ID of the notification.
 * Applicable for devices running Android Oreo ([android.os.Build.VERSION_CODES.O]) and up.
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

    private constructor(builder: Builder) : this(
        builder.notificationActions,
        builder.notificationBody,
        builder.notificationChannelId,
        builder.notificationColor,
        builder.notificationIcon,
        builder.notificationPriority,
        builder.notificationTitle,
        builder.notificationTtl,
        builder.userOrTopic
    )

    companion object {
        /**
         * Creates a [NotificationRequest] using a [Builder] (with support for inlined setting of variables)
         * @return The created [NotificationRequest]
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()

        /**
         * Indicates that the notification should have a normal priority
         */
        const val NOTIFICATION_PRIORITY_NORMAL = "normal"
        /**
         * Indicates that the notification should have a high priority
         */
        const val NOTIFICATION_PRIORITY_HIGH = "high"
    }

    @StringDef(NOTIFICATION_PRIORITY_NORMAL, NOTIFICATION_PRIORITY_HIGH)
    annotation class NotificationPriority


    class Builder {
        /**
         * The notification's actions
         */
        var notificationActions: MutableList<NotificationAction>? = mutableListOf()
        /**
         * The notification's body
         */
        var notificationBody: String? = null
        /**
         * The notification's channel ID
         */
        var notificationChannelId: String? = null
        /**
         * The notification's colour/color
         */
        var notificationColor: String? = null
        /**
         * The notification's icon
         */
        var notificationIcon: String? = null
        /**
         * The notification's priority
         */
        @NotificationPriority
        var notificationPriority: String? = null
        /**
         * The notification's title
         */
        var notificationTitle: String? = null
        /**
         * The notification's Time-To-Live (TTL)
         */
        var notificationTtl: Long? = null
        /**
         * The user/topic to send the notification to
         *
         * Note: Topics should be appended with `topic_`
         */
        var userOrTopic: String? = null

        /**
         * Returns the created [NotificationRequest]
         * @return The created [NotificationRequest]
         */
        fun build() = NotificationRequest(this)
    }
}
