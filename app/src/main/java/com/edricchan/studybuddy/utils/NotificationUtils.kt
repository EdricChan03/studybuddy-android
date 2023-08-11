package com.edricchan.studybuddy.utils

import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationManagerCompat
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.notificationChannelCompat
import com.edricchan.studybuddy.extensions.notificationChannelGroupCompat
import java.util.concurrent.atomic.AtomicInteger

/**
 * Utility class for notification-related functionality.
 * @param initialId The initial ID (Default: `2`)
 */
class NotificationUtils(
    private val initialId: Int = 2
) {
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(
        "Context is no longer used in this class, use the overload that specifies " +
            "only the initialId instead",
        ReplaceWith("NotificationUtils(initialId = initialId)")
    )
    constructor(context: Context? = null, initialId: Int = 2) : this(initialId = initialId)

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

        /**
         * Creates a new instance of the utility class with a context and an initial ID.
         * @param context The context to be used.
         * @param initialId The initial ID to set.
         */
        @Deprecated(
            "Context is no longer used in this class, use the overload that specifies " +
                "only the initialId instead",
            ReplaceWith("NotificationUtils.getInstance(initialId = initialId)")
        )
        fun getInstance(context: Context, initialId: Int) = NotificationUtils(
            context,
            initialId
        )

        /**
         * Sets up the necessary notification channels.
         * @param context The context
         */
        @Deprecated(
            "Use the createNotificationChannelsCompat extension function instead",
            ReplaceWith(
                "context.createNotificationChannelsCompat()",
                "com.edricchan.studybuddy.utils.createNotificationChannelsCompat"
            )
        )
        fun createNotificationChannels(context: Context) {
            context.createNotificationChannelsCompat()
        }
    }
}

fun Context.createNotificationChannelsCompat() {
    val notificationManager = NotificationManagerCompat.from(this)

    val channels = buildList {
        // Task updates notifications
        val todoUpdatesChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_todo_updates_id),
            name = getString(R.string.notification_channel_todo_updates_title),
            description = getString(R.string.notification_channel_todo_updates_desc),
            importance = NotificationManagerCompat.IMPORTANCE_HIGH
        ) {
            setGroup(getString(R.string.notification_channel_group_todos_id))
            setLightsEnabled(true)
            setLightColor(Color.YELLOW)
            setVibrationEnabled(true)
            setShowBadge(true)
        }
        this += todoUpdatesChannel

        // Weekly summary notifications
        val weeklySummaryChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_weekly_summary_id),
            name = getString(R.string.notification_channel_weekly_summary_title),
            description = getString(R.string.notification_channel_weekly_summary_desc),
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        ) {
            setGroup(getString(R.string.notification_channel_group_todos_id))
            setShowBadge(true)
        }
        this += weeklySummaryChannel

        // Syncing notifications
        val syncChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_sync_id),
            name = getString(R.string.notification_channel_sync_title),
            description = getString(R.string.notification_channel_sync_desc),
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        ) {
            setShowBadge(false)
        }
        this += syncChannel

        // Update error notifications
        val updateErrorChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_update_error_id),
            name = getString(R.string.notification_channel_update_error_title),
            description = getString(R.string.notification_channel_update_error_desc),
            importance = NotificationManagerCompat.IMPORTANCE_HIGH
        ) {
            setGroup(getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
        this += updateErrorChannel
        // Update status notifications
        val updateStatusChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_update_status_id),
            name = getString(R.string.notification_channel_update_status_title),
            description = getString(R.string.notification_channel_update_status_desc),
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        ) {
            setGroup(getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
        this += updateStatusChannel
        // Update complete notifications
        val updateCompleteChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_update_complete_id),
            name = getString(R.string.notification_channel_update_complete_title),
            description = getString(R.string.notification_channel_update_complete_desc),
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        ) {
            setGroup(getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
        this += updateCompleteChannel
        // Update not available notifications
        val updateNotAvailableChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_update_not_available_id),
            name = getString(R.string.notification_channel_update_not_available_title),
            description = getString(R.string.notification_channel_update_not_available_desc),
            importance = NotificationManagerCompat.IMPORTANCE_DEFAULT
        ) {
            setGroup(getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
        this += updateNotAvailableChannel
        // Update available notifications
        val updateAvailableChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_update_available_id),
            name = getString(R.string.notification_channel_update_available_title),
            description = getString(R.string.notification_channel_update_available_desc),
            importance = NotificationManagerCompat.IMPORTANCE_HIGH
        ) {
            setGroup(getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
        this += updateAvailableChannel

        // Media playback notifications
        val playbackChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_playback_id),
            name = getString(R.string.notification_channel_playback_title),
            description = getString(R.string.notification_channel_playback_desc),
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        ) {
            // We don't want playback notifications to show a badge
            setShowBadge(false)
        }
        this += playbackChannel

        // New in-app features notifications
        val newFeaturesChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_new_app_features_id),
            name = getString(R.string.notification_channel_new_app_features_title),
            description = getString(R.string.notification_channel_new_app_features_desc),
            importance = NotificationManagerCompat.IMPORTANCE_LOW
        ) {
            setShowBadge(true)
        }
        this += newFeaturesChannel

        // Uncategorised notifications
        val uncategorisedChannel = notificationChannelCompat(
            id = getString(R.string.notification_channel_uncategorised_id),
            name = getString(R.string.notification_channel_uncategorised_title),
            description = getString(R.string.notification_channel_uncategorised_desc),
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        ) {
            setShowBadge(true)
        }
        this += uncategorisedChannel
    }

    val channelGroups = buildList {
        val todoChannelGroup = notificationChannelGroupCompat(
            id = getString(R.string.notification_channel_group_todos_id),
            name = getString(R.string.notification_channel_group_todos_title)
        )
        this += todoChannelGroup
        val updatesChannelGroup = notificationChannelGroupCompat(
            id = getString(R.string.notification_channel_group_updates_id),
            name = getString(R.string.notification_channel_group_updates_title)
        )
        this += updatesChannelGroup
    }

    with(notificationManager) {
        createNotificationChannelGroupsCompat(channelGroups)
        createNotificationChannelsCompat(channels)
    }
}
