package com.edricchan.studybuddy.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.Discouraged
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.buildIntent
import com.edricchan.studybuddy.receivers.NotificationActionReceiver
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity
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
     * Checks whether the network is available
     *
     * @param context The context
     * @return A boolean
     */
    @Deprecated("Use ConnectivityManager.getActiveNetworkInfo")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /**
     * Checks whether the network is cellular
     *
     * @param context The context
     * @return A boolean
     * See https://stackoverflow.com/a/32771164
     * TODO: Use other way of checking for mobile data
     * TODO: Deprecate this method
     */
    fun isCellularNetworkAvailable(context: Context): Boolean {
        try {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                // connected to the mobile provider's data plan
                return activeNetwork.type == ConnectivityManager.TYPE_MOBILE
            }
        } catch (e: Exception) {
            Log.w(
                TAG,
                "An error occurred while attempting to retrieve the cellular network: ",
                e
            )
            return false
        }

        return false
    }

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
            context.getString(R.string.notification_channel_update_status_id)
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

                        val contentIntent = buildIntent<UpdatesActivity>(context) {
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        val pContentIntent =
                            PendingIntent.getActivity(context, 0, contentIntent, 0)

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
                            .setChannelId(context.getString(R.string.notification_channel_update_available_id))
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
                            .setChannelId(context.getString(R.string.notification_channel_update_error_id))
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
