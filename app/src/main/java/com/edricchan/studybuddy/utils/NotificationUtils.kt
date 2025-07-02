package com.edricchan.studybuddy.utils

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.edricchan.studybuddy.core.resources.notification.AppNotificationChannel
import com.edricchan.studybuddy.extensions.notificationChannelGroupCompat
import java.util.concurrent.atomic.AtomicInteger
import com.edricchan.studybuddy.core.resources.R as CoreResR

/**
 * Utility class for notification-related functionality.
 * @param initialId The initial ID (Default: `2`)
 */
class NotificationUtils(
    private val initialId: Int = 2
) {
    private val atomicInteger = AtomicInteger(initialId)

    /**
     * Creates a new ID for a new notification.
     */
    fun incrementAndGetId() = atomicInteger.incrementAndGet()

    /** The current ID. */
    var id: Int
        get() = atomicInteger.get()
        set(value) {
            atomicInteger.set(value)
        }

    companion object {
        /**
         * Creates a new instance of the utility class without any parameters.
         */
        fun getInstance() = NotificationUtils()

        /**
         * Creates a new instance of the utility class with an initial ID.
         * @param initialId The initial ID to set.
         */
        fun getInstance(initialId: Int) = NotificationUtils(initialId = initialId)
    }
}

fun Context.createNotificationChannelsCompat() {
    val notificationManager = NotificationManagerCompat.from(this)

    val channels = AppNotificationChannel.entries.map { it.asNotificationChannel(this) }

    val channelGroups = listOf(
        notificationChannelGroupCompat(
            id = getString(CoreResR.string.notification_channel_group_todos_id),
            name = getString(CoreResR.string.notification_channel_group_todos_title)
        ),
        notificationChannelGroupCompat(
            id = getString(CoreResR.string.notification_channel_group_updates_id),
            name = getString(CoreResR.string.notification_channel_group_updates_title)
        )
    )

    with(notificationManager) {
        createNotificationChannelGroupsCompat(channelGroups)
        createNotificationChannelsCompat(channels)
    }
}
