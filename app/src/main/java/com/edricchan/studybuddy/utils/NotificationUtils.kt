package com.edricchan.studybuddy.utils

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.edricchan.studybuddy.R
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Utility class for notification-related functionality.
 * @param initialId The initial ID (Default: `2`)
 */
class NotificationUtils(
    val context: Context? = null,
    private val initialId: Int = 2
) {
    private val atomicInteger = AtomicInteger(initialId)

    /**
     * Creates a new ID for a new notification.
     */
    fun incrementAndGetId() = atomicInteger.incrementAndGet()

    /**
     * Retrieves the current ID.
     */
    fun getId() = atomicInteger.get()

    /**
     * Explicitly sets the current integer.
     * @param id The ID to use.
     */
    fun setId(id: Int) {
        atomicInteger.set(id)
    }

    /**
     * Sets up the necessary notification channels.
     * Note: The code doesn't include a check for the device's SDK version.
     * @throws IllegalStateException When no [context] is specified.
     */
    fun createNotificationChannels() {
        if (context != null) {
            createNotificationChannels(context)
        } else {
            throw IllegalStateException("A context has to be specified!")
        }
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
        fun getInstance(context: Context, initialId: Int) = NotificationUtils(
            context,
            initialId
        )

        /**
         * Sets up the necessary notification channels.
         * Note: The code doesn't include a check for the device's SDK version.
         * @param context The context
         */
        fun createNotificationChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = NotificationManagerCompat.from(context)
                val channels = ArrayList<NotificationChannel>()
                // Create another list for channel groups
                val channelGroups = ArrayList<NotificationChannelGroup>()

                // Task updates notifications
                val todoUpdatesChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_todo_updates_id),
                    context.getString(R.string.notification_channel_todo_updates_title),
                    NotificationManager.IMPORTANCE_HIGH
                )
                todoUpdatesChannel.description =
                    context.getString(R.string.notification_channel_todo_updates_desc)
                todoUpdatesChannel.group =
                    context.getString(R.string.notification_channel_group_todos_id)
                todoUpdatesChannel.enableLights(true)
                todoUpdatesChannel.lightColor = Color.YELLOW
                todoUpdatesChannel.enableVibration(true)
                todoUpdatesChannel.setShowBadge(true)
                channels.add(todoUpdatesChannel)

                // Weekly summary notifications
                val weeklySummaryChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_weekly_summary_id),
                    context.getString(R.string.notification_channel_weekly_summary_title),
                    NotificationManager.IMPORTANCE_LOW
                )
                weeklySummaryChannel.description =
                    context.getString(R.string.notification_channel_weekly_summary_desc)
                weeklySummaryChannel.group =
                    context.getString(R.string.notification_channel_group_todos_id)
                weeklySummaryChannel.setShowBadge(true)
                channels.add(weeklySummaryChannel)

                // Syncing notifications
                val syncChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_sync_id),
                    context.getString(R.string.notification_channel_sync_title),
                    NotificationManager.IMPORTANCE_LOW
                )
                syncChannel.description = context.getString(R.string.notification_channel_sync_desc)
                syncChannel.setShowBadge(false)
                channels.add(syncChannel)

                // Update error notifications
                val updateErrorChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_update_error_id),
                    context.getString(R.string.notification_channel_update_error_title),
                    NotificationManager.IMPORTANCE_HIGH
                )
                updateErrorChannel.description =
                    context.getString(R.string.notification_channel_update_error_desc)
                updateErrorChannel.group =
                    context.getString(R.string.notification_channel_group_updates_id)
                updateErrorChannel.setShowBadge(false)
                channels.add(updateErrorChannel)
                // Update status notifications
                val updateStatusChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_update_status_id),
                    context.getString(R.string.notification_channel_update_status_title),
                    NotificationManager.IMPORTANCE_LOW
                )
                updateStatusChannel.description =
                    context.getString(R.string.notification_channel_update_status_desc)
                updateStatusChannel.group =
                    context.getString(R.string.notification_channel_group_updates_id)
                updateStatusChannel.setShowBadge(false)
                channels.add(updateStatusChannel)
                // Update complete notifications
                val updateCompleteChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_update_complete_id),
                    context.getString(R.string.notification_channel_update_complete_title),
                    NotificationManager.IMPORTANCE_LOW
                )
                updateCompleteChannel.description =
                    context.getString(R.string.notification_channel_update_complete_desc)
                updateCompleteChannel.group =
                    context.getString(R.string.notification_channel_group_updates_id)
                updateCompleteChannel.setShowBadge(false)
                channels.add(updateCompleteChannel)
                // Update not available notifications
                val updateNotAvailableChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_update_not_available_id),
                    context.getString(R.string.notification_channel_update_not_available_title),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                updateNotAvailableChannel.description =
                    context.getString(R.string.notification_channel_update_not_available_desc)
                updateNotAvailableChannel.group =
                    context.getString(R.string.notification_channel_group_updates_id)
                updateNotAvailableChannel.setShowBadge(false)
                channels.add(updateNotAvailableChannel)
                // Update available notifications
                val updateAvailableChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_update_available_id),
                    context.getString(R.string.notification_channel_update_available_title),
                    NotificationManager.IMPORTANCE_HIGH
                )
                updateAvailableChannel.description =
                    context.getString(R.string.notification_channel_update_available_desc)
                updateAvailableChannel.group =
                    context.getString(R.string.notification_channel_group_updates_id)
                updateAvailableChannel.setShowBadge(false)
                channels.add(updateAvailableChannel)

                // Media playback notifications
                val playbackChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_playback_id),
                    context.getString(R.string.notification_channel_playback_title),
                    NotificationManager.IMPORTANCE_LOW
                )
                playbackChannel.description =
                    context.getString(R.string.notification_channel_playback_desc)
                // We don't want to consider a playback notification to show a badge
                playbackChannel.setShowBadge(false)
                channels.add(playbackChannel)

                // New in-app features notifications
                val newFeaturesChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_new_app_features_id),
                    context.getString(R.string.notification_channel_new_app_features_title),
                    NotificationManager.IMPORTANCE_LOW
                )
                newFeaturesChannel.setShowBadge(true)
                channels.add(newFeaturesChannel)

                // Uncategorised notifications
                val uncategorisedChannel = NotificationChannel(
                    context.getString(R.string.notification_channel_uncategorised_id),
                    context.getString(R.string.notification_channel_uncategorised_title),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                uncategorisedChannel.description =
                    context.getString(R.string.notification_channel_uncategorised_desc)
                uncategorisedChannel.setShowBadge(true)
                channels.add(uncategorisedChannel)

                // Notification channel groups
                val todoChannelGroup = NotificationChannelGroup(
                    context.getString(R.string.notification_channel_group_todos_id),
                    context.getString(R.string.notification_channel_group_todos_title)
                )
                channelGroups.add(todoChannelGroup)
                val updatesChannelGroup = NotificationChannelGroup(
                    context.getString(R.string.notification_channel_group_updates_id),
                    context.getString(R.string.notification_channel_group_updates_title)
                )
                channelGroups.add(updatesChannelGroup)
                notificationManager.createNotificationChannelGroups(channelGroups)

                // Pass list to method
                notificationManager.createNotificationChannels(channels)

            }
        }
    }
}