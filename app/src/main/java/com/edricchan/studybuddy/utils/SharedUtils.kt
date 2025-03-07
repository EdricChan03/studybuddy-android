package com.edricchan.studybuddy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.Discouraged
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.compat.navigation.UriUpdates
import com.edricchan.studybuddy.core.resources.notification.AppNotificationChannel
import com.edricchan.studybuddy.exts.android.buildIntent
import com.edricchan.studybuddy.exts.android.createPendingIntent
import com.edricchan.studybuddy.receivers.NotificationActionReceiver
import com.edricchan.studybuddy.ui.modules.main.MainActivity
import com.edricchan.studybuddy.ui.theming.dynamicColorPrimary
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update

/**
 * Shared utility methods
 */
object SharedUtils {
    /**
     * An utility method to check for updates.
     *
     * @param context The context.
     */
    @Discouraged(message = "Consider using the `CheckForUpdatesWorker` worker instead")
    fun checkForUpdates(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)
        val notifyBuilder = NotificationCompat.Builder(
            context,
            AppNotificationChannel.UpdateStatus.channelId
        )
            .setSmallIcon(R.drawable.ic_notification_system_update_24dp)
            .setContentTitle(context.getString(R.string.notification_check_update))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setProgress(100, 0, true)
            .setColor(context.dynamicColorPrimary)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
        notificationManager.notify(
            Constants.notificationCheckForUpdatesId,
            notifyBuilder.build()
        )
        AppUpdaterUtils(context)
            .setUpdateFrom(UpdateFrom.JSON)
            .setUpdateJSON(context.getUpdateJsonUrl())
            .withListener(object : AppUpdaterUtils.UpdateListener {
                override fun onSuccess(update: Update, updateAvailable: Boolean?) {
                    if (update.latestVersionCode == BuildConfig.VERSION_CODE && (!updateAvailable!!)) {
                        // User is running latest version
                        notifyBuilder.setContentTitle(context.getString(R.string.notification_no_updates))
                            .setProgress(0, 0, false)
                            .setCategory(null)
                            .setOngoing(false)
                        notificationManager.notify(
                            Constants.notificationCheckForUpdatesId,
                            notifyBuilder.build()
                        )
                    } else {
                        // New update
                        val intentAction =
                            Intent(context, NotificationActionReceiver::class.java)
                        intentAction.putExtra(
                            "action",
                            Constants.actionNotificationsStartDownloadReceiver
                        )
                        intentAction.putExtra(
                            "downloadUrl",
                            update.urlToDownload.toString()
                        )
                        intentAction.putExtra("version", update.latestVersion)
                        val pIntentDownload = PendingIntent.getBroadcast(
                            context,
                            1,
                            intentAction,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        val pContentIntent =
                            context.createPendingIntent(requestCode = 0, flags = 0) {
                                addNextIntentWithParentStack(buildIntent<MainActivity>(context) {
                                    action = Intent.ACTION_VIEW
                                    data = UriUpdates.toUri()
                                })
                            }

                        notifyBuilder.setContentTitle(context.getString(R.string.notification_new_update_title))
                            .setContentText(
                                context.getString(
                                    R.string.notification_new_update_text,
                                    update.latestVersion
                                )
                            )
                            .setProgress(0, 0, false)
                            .setCategory(null)
                            .setOngoing(false)
                            .setChannelId(AppNotificationChannel.UpdateAvailable.channelId)
                            .setContentIntent(pContentIntent)
                            .setAutoCancel(true)
                            .addAction(
                                NotificationCompat.Action(
                                    R.drawable.ic_download_24dp,
                                    "Download",
                                    pIntentDownload
                                )
                            )
                        notificationManager.notify(
                            Constants.notificationCheckForUpdatesId,
                            notifyBuilder.build()
                        )
                    }
                }

                override fun onFailed(appUpdaterError: AppUpdaterError) {
                    when (appUpdaterError) {
                        AppUpdaterError.NETWORK_NOT_AVAILABLE -> notifyBuilder.setContentTitle(
                            context.getString(R.string.notification_updates_error_no_internet_title)
                        )
                            .setContentText(context.getString(R.string.notification_updates_error_no_internet_text))
                            .setSmallIcon(R.drawable.ic_wifi_strength_4_alert_24dp)

                        AppUpdaterError.JSON_ERROR -> notifyBuilder.setContentTitle(
                            context.getString(
                                R.string.notification_updates_error_not_found_title
                            )
                        )
                            .setContentText(context.getString(R.string.notification_updates_error_not_found_text))
                            .setSmallIcon(R.drawable.ic_file_not_found_24dp)

                        else -> notifyBuilder.setContentTitle(
                            context.getString(R.string.notification_updates_error_unknown_title)
                        )
                            .setContentText(
                                context.getString(R.string.notification_updates_error_unknown_text)
                            )
                            .setSmallIcon(R.drawable.ic_warning_24dp)
                    }
                    val intentAction =
                        Intent(context, NotificationActionReceiver::class.java)

                    //This is optional if you have more than one buttons and want to differentiate between two
                    intentAction.putExtra(
                        "action",
                        Constants.actionNotificationsRetryCheckForUpdateReceiver
                    )
                    val pIntentRetry = PendingIntent.getBroadcast(
                        context,
                        2,
                        intentAction,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    notificationManager.notify(
                        Constants.notificationCheckForUpdatesId,
                        notifyBuilder
                            .setProgress(0, 0, false)
                            .setCategory(NotificationCompat.CATEGORY_ERROR)
                            .setOngoing(false)
                            .setChannelId(AppNotificationChannel.UpdateError.channelId)
                            .setColor(context.dynamicColorPrimary)
                            .addAction(
                                NotificationCompat.Action(
                                    R.drawable.ic_refresh_24dp,
                                    "Retry",
                                    pIntentRetry
                                )
                            )
                            .setStyle(NotificationCompat.BigTextStyle())
                            .build()
                    )
                }
            })
            .start()
    }
}
