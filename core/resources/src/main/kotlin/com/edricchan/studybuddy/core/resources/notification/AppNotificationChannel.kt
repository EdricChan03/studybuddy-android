package com.edricchan.studybuddy.core.resources.notification

import android.content.Context
import android.graphics.Color
import androidx.annotation.StringRes
import androidx.core.app.NotificationChannelCompat
import com.edricchan.studybuddy.core.resources.R

enum class AppNotificationChannel(
    val channelId: String,
    @StringRes
    val titleRes: Int,
    @StringRes
    val descRes: Int,
    val importance: NotificationImportance = NotificationImportance.Unspecified
) {
    // General group
    NewAppFeatures(
        channelId = "new_app_features",
        titleRes = R.string.notification_channel_new_app_features_title,
        descRes = R.string.notification_channel_new_app_features_desc,
        importance = NotificationImportance.Low
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setShowBadge(true)
        }
    },
    Playback(
        channelId = "playback",
        titleRes = R.string.notification_channel_playback_title,
        descRes = R.string.notification_channel_playback_desc,
        importance = NotificationImportance.Low
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            // We don't want playback notifications to show a badge
            setShowBadge(false)
        }
    },
    Sync(
        channelId = "sync",
        titleRes = R.string.notification_channel_sync_title,
        descRes = R.string.notification_channel_sync_desc,
        importance = NotificationImportance.Low
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setShowBadge(false)
        }
    },
    Uncategorized(
        channelId = "uncategorised",
        titleRes = R.string.notification_channel_uncategorised_title,
        descRes = R.string.notification_channel_uncategorised_desc,
        importance = NotificationImportance.Default
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setShowBadge(true)
        }
    },

    // Task group
    TaskUpdates(
        channelId = "todo_updates",
        titleRes = R.string.notification_channel_todo_updates_title,
        descRes = R.string.notification_channel_todo_updates_desc,
        importance = NotificationImportance.High
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setGroup(context.getString(R.string.notification_channel_group_todos_id))
            setLightsEnabled(true)
            setLightColor(Color.YELLOW)
            setVibrationEnabled(true)
            setShowBadge(true)
        }
    },
    WeeklySummary(
        channelId = "weekly_summary",
        titleRes = R.string.notification_channel_weekly_summary_title,
        descRes = R.string.notification_channel_weekly_summary_desc,
        importance = NotificationImportance.Low
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setGroup(context.getString(R.string.notification_channel_group_todos_id))
            setShowBadge(true)
        }
    },

    // Update group
    UpdateAvailable(
        channelId = "update_available",
        titleRes = R.string.notification_channel_update_available_title,
        descRes = R.string.notification_channel_update_available_desc,
        importance = NotificationImportance.High
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setGroup(context.getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
    },
    UpdateComplete(
        channelId = "update_complete",
        titleRes = R.string.notification_channel_update_complete_title,
        descRes = R.string.notification_channel_update_complete_desc,
        importance = NotificationImportance.Low
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setGroup(context.getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
    },
    UpdateError(
        channelId = "update_error",
        titleRes = R.string.notification_channel_update_error_title,
        descRes = R.string.notification_channel_update_error_desc,
        importance = NotificationImportance.High
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setGroup(context.getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
    },
    UpdateUnavailable(
        channelId = "update_not_available",
        titleRes = R.string.notification_channel_update_not_available_title,
        descRes = R.string.notification_channel_update_not_available_desc,
        importance = NotificationImportance.Default
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setGroup(context.getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
    },
    UpdateStatus(
        channelId = "update_status",
        titleRes = R.string.notification_channel_update_status_title,
        descRes = R.string.notification_channel_update_status_desc,
        importance = NotificationImportance.Low
    ) {
        override fun NotificationChannelCompat.Builder.onCreateNotificationChannel(context: Context) {
            setGroup(context.getString(R.string.notification_channel_group_updates_id))
            setShowBadge(false)
        }
    };

    protected open fun NotificationChannelCompat.Builder.onCreateNotificationChannel(
        context: Context
    ) {
    }

    /**
     * Converts this [AppNotificationChannel] to its [NotificationChannelCompat] equivalent.
     * @param context [Context] to be used for [onCreateNotificationChannel].
     */
    fun asNotificationChannel(
        context: Context
    ): NotificationChannelCompat = NotificationChannelCompat.Builder(channelId, importance.value)
        .apply {
            setName(context.getString(titleRes))
            setDescription(context.getString(descRes))
            onCreateNotificationChannel(context)
        }.build()
}
